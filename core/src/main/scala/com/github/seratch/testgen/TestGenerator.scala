/*
 * Copyright 2011 Kazuhiro SERA.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package com.github.seratch.testgen

import java.lang.StringBuilder

class TestGenerator(val config: Config) {

  private val CRLF = "\r\n"

  private val INDENT = "  "

  class CodeBuilder {

    val buf = new StringBuilder

    def +=(str: String): CodeBuilder = {
      buf.append(str)
      this
    }

    override def toString() = buf.toString

  }

  def generate(target: Target): Test = {
    val toImportList = List(
      "org.scalatest._",
      "org.scalatest.matchers._",
      "org.junit.runner.RunWith",
      "org.scalatest.junit.JUnitRunner"
    ) ::: target.importList
    val code = new CodeBuilder
    code += "package " += target.fullPackageName += CRLF
    code += CRLF
    toImportList foreach {
      case toImport if toImport.endsWith(".") => code += "import " += toImport + "_" += CRLF
      case toImport => code += "import " += toImport += CRLF
    }
    code += CRLF
    code += "@RunWith(classOf[JUnitRunner])" += CRLF
    // TODO
    val toExtend = config.testTemplate match {
      case TestTemplate.FunSuite => "extends FunSuite with ShouldMatchers"
      case _ => throw new UnsupportedOperationException
    }
    val suffix = "Suite"
    val testClassName = target.typeName + suffix
    code += "class " += testClassName += " " += toExtend += " {" += CRLF
    code += CRLF
    code += INDENT += "type ? = this.type" += CRLF
    code += CRLF
    code += INDENT += """test("available") {""" += CRLF
    target.defType match {
      case DefType.Class => {
        target.parameters match {
          case Nil => {
            code += INDENT * 2 += "val instance = new " += target.typeName += "()" += CRLF
          }
          case params => {
            params foreach {
              case param => {
                code += INDENT * 2 += "val " += param.name += ": " += param.typeName += " = null" += CRLF
              }
            }
            code += INDENT * 2 += "val instance = new " += target.typeName += "("
            val paramArea = new CodeBuilder
            params foreach {
              case param => paramArea += param.name += ","
            }
            code += paramArea.toString.replaceFirst(",$", "") += ")" += CRLF
          }
        }
        code += INDENT * 2 += "instance should not be null" += CRLF
      }
      case DefType.Object => {
        code += INDENT * 2 += target.typeName += ".isInstanceOf[Singleton] should equal(true)" += CRLF
      }
      case DefType.Trait => {
        code += INDENT * 2 += "val mixined = new Object with " += target.typeName += CRLF
        code += INDENT * 2 += "mixined should not be null" += CRLF
      }
      case _ =>
    }
    code += INDENT += """}""" += CRLF
    code += CRLF
    code += "}" += CRLF
    new Test(
      config = config,
      fullPackageName = target.fullPackageName,
      testClassName = testClassName,
      sourceCode = code.toString
    )
  }

}