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
package testgen

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

    val isScalaTest = config.testTemplate != TestTemplate.SpecsSpecification &&
      config.testTemplate != TestTemplate.Specs2Specification

    val toImportList = (config.testTemplate match {
      case TestTemplate.SpecsSpecification => List(
        "org.specs.Specification",
        "org.junit.runner.RunWith",
        "org.scalatest.junit.JUnitRunner"
      )
      case TestTemplate.Specs2Specification => List(
        "org.specs2.mutable.Specification",
        "org.junit.runner.RunWith",
        "org.scalatest.junit.JUnitRunner"
      )
      case TestTemplate.ScalaTestAssertions => List(
        "org.scalatest._",
        "org.scalatest.matchers._",
        "org.junit.Test"
      )
      case _ => List(
        "org.scalatest._",
        "org.scalatest.matchers._",
        "org.junit.runner.RunWith",
        "org.scalatest.junit.JUnitRunner"
      )
    }) ::: target.importList

    val code = new CodeBuilder
    code += "package " += target.fullPackageName += CRLF
    code += CRLF
    toImportList foreach {
      case toImport if toImport.endsWith(".") => code += "import " += toImport + "_" += CRLF
      case toImport => code += "import " += toImport += CRLF
    }
    code += CRLF
    config.testTemplate match {
      case TestTemplate.ScalaTestAssertions =>
      case _ => code += "@RunWith(classOf[JUnitRunner])" += CRLF
    }
    val toExtend = config.testTemplate match {
      case TestTemplate.ScalaTestFunSuite => "extends FunSuite"
      case TestTemplate.ScalaTestAssertions => "extends Assertions"
      case TestTemplate.ScalaTestSpec => "extends Spec"
      case TestTemplate.ScalaTestWordSpec => "extends WordSpec"
      case TestTemplate.ScalaTestFlatSpec => "extends FlatSpec"
      case TestTemplate.ScalaTestFeatureSpec => "extends FeatureSpec"
      case TestTemplate.SpecsSpecification => "extends Specification"
      case TestTemplate.Specs2Specification => "extends Specification"
      case _ => throw new UnsupportedOperationException
    }
    val toMixIn = if (isScalaTest) {
      config.scalaTestMatchers match {
        case ScalaTestMatchers.Should => " with ShouldMatchers"
        case ScalaTestMatchers.Must => " with MustMatchers"
        case _ => ""
      }
    } else ""

    val suffix = config.testTemplate match {
      case TestTemplate.ScalaTestFunSuite | TestTemplate.ScalaTestAssertions => "Suite"
      case _ => "Spec"
    }
    val testClassName = target.typeName + suffix
    code += "class " += testClassName += " " += toExtend += toMixIn += " {" += CRLF
    if (isScalaTest) {
      code += CRLF
      code += INDENT += "type ? = this.type // for IntelliJ IDEA" += CRLF
    }
    code += CRLF

    var depth = 1
    config.testTemplate match {
      case TestTemplate.ScalaTestFunSuite => {
        code += INDENT * depth += """test("available") {""" += CRLF
        depth += 1
      }
      case TestTemplate.ScalaTestAssertions => {
        code += INDENT * depth += "@Test def available {" += CRLF
        depth += 1
      }
      case TestTemplate.ScalaTestSpec => {
        code += INDENT * depth += "describe(\"" + target.typeName + "\") {" += CRLF
        depth += 1
        code += INDENT * depth += "it(\"should be available\") {" += CRLF
        depth += 1
      }
      case TestTemplate.ScalaTestWordSpec => {
        code += INDENT += "\"" + target.typeName + "\" should {" += CRLF
        depth += 1
        code += INDENT * depth += "\"be available\" in {" += CRLF
        depth += 1
      }
      case TestTemplate.ScalaTestFlatSpec => {
        code += INDENT * depth += "\"" + target.typeName + "\" should \"be available\" in {" += CRLF
        depth += 1
      }
      case TestTemplate.ScalaTestFeatureSpec => {
        code += INDENT * depth += "feature(\"" + target.typeName + "\") {" += CRLF
        depth += 1
        code += INDENT * depth += "scenario(\"it is prepared\") {" += CRLF
        depth += 1
      }
      case TestTemplate.SpecsSpecification | TestTemplate.Specs2Specification => {
        code += INDENT * depth += "\"" + target.typeName + "\" should {" += CRLF
        depth += 1
        code += INDENT * depth += "\"be available\" in {" += CRLF
        depth += 1
      }
    }

    target.defType match {
      case DefType.Class => {
        target.parameters match {
          case Nil => {
            code += INDENT * depth += "val instance = new " += target.typeName += "()" += CRLF
          }
          case params => {
            val indentAndValDef = INDENT * depth + "val "
            params foreach {
              case p if p.typeName == "Byte" || p.typeName == "Int" || p.typeName == "Short" =>
                code += indentAndValDef += p.name += ": " += p.typeName += " = 0" += CRLF
              case p if p.typeName == "Long" =>
                code += indentAndValDef += p.name += ": " += p.typeName += " = 0L" += CRLF
              case p if p.typeName == "Double" =>
                code += indentAndValDef += p.name += ": " += p.typeName += " = 0D" += CRLF
              case p if p.typeName == "Float" =>
                code += indentAndValDef += p.name += ": " += p.typeName += " = 0F" += CRLF
              case p if p.typeName == "Boolean" =>
                code += indentAndValDef += p.name += ": " += p.typeName += " = false" += CRLF
              case p if p.typeName == "Char" =>
                code += indentAndValDef += p.name += ": " += p.typeName += " = ' '" += CRLF
              case p if p.typeName == "String" =>
                code += indentAndValDef += p.name += ": " += p.typeName += " = \"\"" += CRLF
              case p if p.typeName == "Seq" =>
                code += indentAndValDef += p.name += ": " += p.typeName += "[_] = Nil" += CRLF
              case p if p.typeName == "Set" =>
                code += indentAndValDef += p.name += ": " += p.typeName += "[_] = Set()" += CRLF
              case p if p.typeName == "List" =>
                code += indentAndValDef += p.name += ": " += p.typeName += "[_] = Nil" += CRLF
              case p if p.typeName == "Array" =>
                code += indentAndValDef += p.name += ": " += p.typeName += "[_] = Array()" += CRLF
              case p if p.typeName == "Stream" =>
                code += indentAndValDef += p.name += ": " += p.typeName += "[_] = Stream()" += CRLF
              case p if p.typeName == "Map" =>
                code += indentAndValDef += p.name += ": " += p.typeName += "[_, _] = Map()" += CRLF
              case p if p.typeName == "Option" =>
                code += indentAndValDef += p.name += ": " += p.typeName += "[_] = None" += CRLF
              case p =>
                code += indentAndValDef += p.name += ": " += p.typeName += " = null" += CRLF
            }
            code += INDENT * depth += "val instance = new " += target.typeName += "("
            val paramArea = new CodeBuilder
            params foreach {
              case param => paramArea += param.name += ","
            }
            code += paramArea.toString.replaceFirst(",$", "") += ")" += CRLF
          }
        }
        if (isScalaTest) {
          config.scalaTestMatchers match {
            case ScalaTestMatchers.Should => code += INDENT * depth += "instance should not be null" += CRLF
            case ScalaTestMatchers.Must => code += INDENT * depth += "instance must not be null" += CRLF
            case _ => code += INDENT * depth += "assert(instance != null)" += CRLF
          }
        } else {
          config.testTemplate match {
            case TestTemplate.SpecsSpecification => {
              code += INDENT * depth += "instance must notBeNull" += CRLF
            }
            case TestTemplate.Specs2Specification => {
              code += INDENT * depth += "instance must not beNull" += CRLF
            }
          }
        }
      }
      case DefType.Object => {
        if (isScalaTest) {
          config.scalaTestMatchers match {
            case ScalaTestMatchers.Should => {
              code += INDENT * depth += target.typeName += ".isInstanceOf[Singleton] should equal(true)" += CRLF
            }
            case ScalaTestMatchers.Must => {
              code += INDENT * depth += target.typeName += ".isInstanceOf[Singleton] must equal(true)" += CRLF
            }
            case _ => {
              code += INDENT * depth += "assert(" += target.typeName += ".isInstanceOf[Singleton])" += CRLF
            }
          }
        } else {
          config.testTemplate match {
            case TestTemplate.SpecsSpecification => {
              code += INDENT * depth += target.typeName += ".isInstanceOf[Singleton] must beEqual(true)" += CRLF
            }
            case TestTemplate.Specs2Specification => {
              code += INDENT * depth += target.typeName += ".isInstanceOf[Singleton] must beEqual(true)" += CRLF
            }
          }
        }
      }
      case DefType.Trait => {
        code += INDENT * depth += "val mixedin = new Object with " += target.typeName += CRLF
        if (isScalaTest) {
          config.scalaTestMatchers match {
            case ScalaTestMatchers.Should => code += INDENT * depth += "mixedin should not be null" += CRLF
            case ScalaTestMatchers.Must => code += INDENT * depth += "mixedin must not be null" += CRLF
            case _ => code += INDENT * depth += "assert(mixedin != null)" += CRLF
          }
        } else {
          config.testTemplate match {
            case TestTemplate.SpecsSpecification => {
              code += INDENT * depth += "mixedin must notBeNull" += CRLF
            }
            case TestTemplate.Specs2Specification => {
              code += INDENT * depth += "mixedin must not beNull" += CRLF
            }
          }
        }
      }
      case _ =>
    }

    config.testTemplate match {
      case TestTemplate.ScalaTestFunSuite => {
        code += INDENT += "}" += CRLF
      }
      case TestTemplate.ScalaTestAssertions => {
        code += INDENT += "}" += CRLF
      }
      case TestTemplate.ScalaTestSpec => {
        code += INDENT * 2 += "}" += CRLF
        code += INDENT += "}" += CRLF
      }
      case TestTemplate.ScalaTestWordSpec => {
        code += INDENT * 2 += "}" += CRLF
        code += INDENT += "}" += CRLF
      }
      case TestTemplate.ScalaTestFlatSpec => {
        code += INDENT += "}" += CRLF
      }
      case TestTemplate.ScalaTestFeatureSpec => {
        code += INDENT * 2 += "}" += CRLF
        code += INDENT += "}" += CRLF
      }
      case TestTemplate.SpecsSpecification | TestTemplate.Specs2Specification => {
        code += INDENT * 2 += "}" += CRLF
        code += INDENT += "}" += CRLF
      }
    }
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