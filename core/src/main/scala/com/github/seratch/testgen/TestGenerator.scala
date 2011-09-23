package com.github.seratch.testgen

import java.lang.StringBuilder

class TestGenerator {

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
      case toImport => code += "import " += toImport += CRLF
    }
    code += CRLF
    code += "@RunWith(classOf[JUnitRunner])" += CRLF
    // TODO
    val toExtend = "extends FunSuite with ShouldMatchers"
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
      fullPackageName = target.fullPackageName,
      testClassName = testClassName,
      sourceCode = code.toString
    )
  }

}