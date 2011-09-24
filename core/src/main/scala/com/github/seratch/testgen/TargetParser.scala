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

import util.parsing.combinator.JavaTokenParsers

case class TargetParser(fullPackageName: String, importList: List[String]) extends JavaTokenParsers {

  object white {
    val rep = "\\s+"
    val maybeRep = "\\s*"
  }

  type P[T] = Parser[T]

  def typeName: P[String] = white.maybeRep.r ~> ident <~ white.maybeRep.r

  // TODO default value

  def args = repsep(ident ~ ":" ~ ident, ",") ^^ {
    case argList => argList map {
      case name ~ ":" ~ typeName => (name, typeName)
    }
  }

  def classDef = ("class" + white.rep).r ~> typeName ^^ {
    name => {
      new Target(
        fullPackageName = fullPackageName,
        defType = DefType.Class,
        importList = importList,
        typeName = name
      )
    }
  }

  def classWithConstructorDef = ("class" + white.rep).r ~> typeName ~ "(" ~ args <~ ")" ^^ {
    case name ~ "(" ~ args => {
      new Target(
        fullPackageName = fullPackageName,
        defType = DefType.Class,
        importList = importList,
        typeName = name,
        parameters = args map {
          case (name, typeName) => new TargetParameter(name, typeName)
        }
      )
    }
  }

  def traitDef = ("trait" + white.rep).r ~> typeName ^^ {
    name => new Target(
      fullPackageName = fullPackageName,
      importList = importList,
      defType = DefType.Trait,
      typeName = name
    )
  }

  def objectDef = ("object" + white.rep).r ~> typeName ^^ {
    name => new Target(
      fullPackageName = fullPackageName,
      importList = importList,
      defType = DefType.Object,
      typeName = name
    )
  }

  def allDef = classWithConstructorDef | classDef | traitDef | objectDef

  def parse(t: P[Target], input: String): ParseResult[List[Target]] = parseAll(rep(t), input)

  def parse(input: String): ParseResult[List[Target]] = parseAll(rep(allDef), input)

}
