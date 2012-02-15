/*
 * Copyright 2011 Kazuhiro Sera.
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

  private def lineBreak: String = config.lineBreak.value

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
    code += "package " += target.fullPackageName += lineBreak
    code += lineBreak
    toImportList foreach {
      case toImport if toImport.endsWith(".") => code += "import " += toImport + "_" += lineBreak
      case toImport => code += "import " += toImport += lineBreak
    }
    code += lineBreak
    config.testTemplate match {
      case TestTemplate.ScalaTestAssertions =>
      case _ => code += "@RunWith(classOf[JUnitRunner])" += lineBreak
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
    code += "class " += testClassName += " " += toExtend += toMixIn += " {" += lineBreak
    code += lineBreak

    var depth = 1
    config.testTemplate match {
      case TestTemplate.ScalaTestFunSuite => {
        code += INDENT * depth += """test("available") {""" += lineBreak
        depth += 1
      }
      case TestTemplate.ScalaTestAssertions => {
        code += INDENT * depth += "@Test def available {" += lineBreak
        depth += 1
      }
      case TestTemplate.ScalaTestSpec => {
        code += INDENT * depth += "describe(\"" + target.typeName + "\") {" += lineBreak
        depth += 1
        code += INDENT * depth += "it(\"should be available\") {" += lineBreak
        depth += 1
      }
      case TestTemplate.ScalaTestWordSpec => {
        code += INDENT += "\"" + target.typeName + "\" should {" += lineBreak
        depth += 1
        code += INDENT * depth += "\"be available\" in {" += lineBreak
        depth += 1
      }
      case TestTemplate.ScalaTestFlatSpec => {
        code += INDENT * depth += "behavior of " + "\"" + target.typeName + "\"" + lineBreak
        code += lineBreak
        code += INDENT * depth += "it should \"be available\" in {" += lineBreak
        depth += 1
      }
      case TestTemplate.ScalaTestFeatureSpec => {
        code += INDENT * depth += "feature(\"" + target.typeName + "\") {" += lineBreak
        depth += 1
        code += INDENT * depth += "scenario(\"it is prepared\") {" += lineBreak
        depth += 1
      }
      case TestTemplate.SpecsSpecification | TestTemplate.Specs2Specification => {
        code += INDENT * depth += "\"" + target.typeName + "\" should {" += lineBreak
        depth += 1
        code += INDENT * depth += "\"be available\" in {" += lineBreak
        depth += 1
      }
    }

    target.defType match {
      case DefType.Class => {
        target.parameters match {
          case Nil => {
            code += INDENT * depth += "val instance = new " += target.typeName += "()" += lineBreak
          }
          case params => {
            val indentAndValDef = INDENT * depth + "val "
            params foreach {
              case p if p.typeName == "Byte" || p.typeName == "Int" || p.typeName == "Short" =>
                code += indentAndValDef += p.name += ": " += p.typeName += " = 0" += lineBreak
              case p if p.typeName == "Long" =>
                code += indentAndValDef += p.name += ": " += p.typeName += " = 0L" += lineBreak
              case p if p.typeName == "Double" =>
                code += indentAndValDef += p.name += ": " += p.typeName += " = 0D" += lineBreak
              case p if p.typeName == "Float" =>
                code += indentAndValDef += p.name += ": " += p.typeName += " = 0F" += lineBreak
              case p if p.typeName == "Boolean" =>
                code += indentAndValDef += p.name += ": " += p.typeName += " = false" += lineBreak
              case p if p.typeName == "Char" =>
                code += indentAndValDef += p.name += ": " += p.typeName += " = ' '" += lineBreak
              case p if p.typeName == "String" =>
                code += indentAndValDef += p.name += ": " += p.typeName += " = \"\"" += lineBreak
              case p if p.typeName == "Seq" =>
                code += indentAndValDef += p.name += ": " += p.typeName += "[_] = Nil" += lineBreak
              case p if p.typeName == "Set" =>
                code += indentAndValDef += p.name += ": " += p.typeName += "[_] = Set()" += lineBreak
              case p if p.typeName == "List" =>
                code += indentAndValDef += p.name += ": " += p.typeName += "[_] = Nil" += lineBreak
              case p if p.typeName == "Array" =>
                code += indentAndValDef += p.name += ": " += p.typeName += "[_] = Array()" += lineBreak
              case p if p.typeName == "Stream" =>
                code += indentAndValDef += p.name += ": " += p.typeName += "[_] = Stream()" += lineBreak
              case p if p.typeName == "Map" =>
                code += indentAndValDef += p.name += ": " += p.typeName += "[_, _] = Map()" += lineBreak
              case p if p.typeName == "Option" =>
                code += indentAndValDef += p.name += ": " += p.typeName += "[_] = None" += lineBreak
              case p =>
                code += indentAndValDef += p.name += ": " += p.typeName += " = null" += lineBreak
            }
            code += INDENT * depth += "val instance = new " += target.typeName += "("
            val paramArea = new CodeBuilder
            params foreach {
              case param => paramArea += param.name += ","
            }
            code += paramArea.toString.replaceFirst(",$", "") += ")" += lineBreak
          }
        }
        if (isScalaTest) {
          config.scalaTestMatchers match {
            case ScalaTestMatchers.Should => code += INDENT * depth += "instance should not be null" += lineBreak
            case ScalaTestMatchers.Must => code += INDENT * depth += "instance must not be null" += lineBreak
            case _ => code += INDENT * depth += "assert(instance != null)" += lineBreak
          }
        } else {
          config.testTemplate match {
            case TestTemplate.SpecsSpecification => {
              code += INDENT * depth += "instance must notBeNull" += lineBreak
            }
            case TestTemplate.Specs2Specification => {
              code += INDENT * depth += "instance must not beNull" += lineBreak
            }
          }
        }
      }
      case DefType.Object => {
        if (isScalaTest) {
          code += INDENT * depth += "val singleton = " + target.typeName += lineBreak
          config.scalaTestMatchers match {
            case ScalaTestMatchers.Should => {
              code += INDENT * depth += "singleton should not be null" += lineBreak
            }
            case ScalaTestMatchers.Must => {
              code += INDENT * depth += "singleton must not be null" += lineBreak
            }
            case _ => {
              code += INDENT * depth += "assert(singleton != null)" += lineBreak
            }
          }
        } else {
          config.testTemplate match {
            case TestTemplate.SpecsSpecification => {
              code += INDENT * depth += "singleton must notBeNull" += lineBreak
            }
            case TestTemplate.Specs2Specification => {
              code += INDENT * depth += "singleton must not beNull" += lineBreak
            }
          }
        }
      }
      case DefType.Trait => {
        code += INDENT * depth += "val mixedin = new Object with " += target.typeName += lineBreak
        if (isScalaTest) {
          config.scalaTestMatchers match {
            case ScalaTestMatchers.Should => code += INDENT * depth += "mixedin should not be null" += lineBreak
            case ScalaTestMatchers.Must => code += INDENT * depth += "mixedin must not be null" += lineBreak
            case _ => code += INDENT * depth += "assert(mixedin != null)" += lineBreak
          }
        } else {
          config.testTemplate match {
            case TestTemplate.SpecsSpecification => {
              code += INDENT * depth += "mixedin must notBeNull" += lineBreak
            }
            case TestTemplate.Specs2Specification => {
              code += INDENT * depth += "mixedin must not beNull" += lineBreak
            }
          }
        }
      }
      case _ =>
    }

    config.testTemplate match {
      case TestTemplate.ScalaTestFunSuite => {
        code += INDENT += "}" += lineBreak
      }
      case TestTemplate.ScalaTestAssertions => {
        code += INDENT += "}" += lineBreak
      }
      case TestTemplate.ScalaTestSpec => {
        code += INDENT * 2 += "}" += lineBreak
        code += INDENT += "}" += lineBreak
      }
      case TestTemplate.ScalaTestWordSpec => {
        code += INDENT * 2 += "}" += lineBreak
        code += INDENT += "}" += lineBreak
      }
      case TestTemplate.ScalaTestFlatSpec => {
        code += INDENT += "}" += lineBreak
      }
      case TestTemplate.ScalaTestFeatureSpec => {
        code += INDENT * 2 += "}" += lineBreak
        code += INDENT += "}" += lineBreak
      }
      case TestTemplate.SpecsSpecification | TestTemplate.Specs2Specification => {
        code += INDENT * 2 += "}" += lineBreak
        code += INDENT += "}" += lineBreak
      }
    }
    code += lineBreak
    code += "}" += lineBreak
    new Test(
      config = config,
      fullPackageName = target.fullPackageName,
      testClassName = testClassName,
      sourceCode = code.toString
    )
  }

}
