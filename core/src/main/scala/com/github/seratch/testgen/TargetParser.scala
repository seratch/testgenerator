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

  type P[T] = Parser[T]

  def variableName = "\\w+".r

  def typeName = (variableName <~ "[" <~ rep(variableName | ",") <~ "]") | variableName

  def argsWithDefaultValue = variableName ~ ":" ~ typeName <~ "=" <~ argDefaultValue <~ ","

  def argsWithoutDefaultValue = variableName ~ ":" ~ typeName <~ ","

  def args = rep(argsWithDefaultValue | argsWithoutDefaultValue) ^^ {
    case argList => argList map {
      case name ~ ":" ~ typeName => (name, typeName)
    }
  }

  def literal = "[\\w\"']+".r

  def newLiteral: P[Any] = rep(variableName) ~ "(" ~ argsInNewLiteral ~ ")"

  def argsInNewLiteral = rep(((newLiteral | literal) ~ ",") | newLiteral | literal)

  def argDefaultValue = newLiteral | literal

  def classDef = "class" ~> typeName ^^ {
    name => {
      new Target(
        fullPackageName = fullPackageName,
        defType = DefType.Class,
        importList = importList,
        typeName = name
      )
    }
  }

  def classWithConstructorDef = "class" ~> typeName ~ "(" ~ args <~ ")" ^^ {
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

  def traitDef = "trait" ~> typeName ^^ {
    name => new Target(
      fullPackageName = fullPackageName,
      importList = importList,
      defType = DefType.Trait,
      typeName = name
    )
  }

  def objectDef = "object" ~> typeName ^^ {
    name => new Target(
      fullPackageName = fullPackageName,
      importList = importList,
      defType = DefType.Object,
      typeName = name
    )
  }

  def allDef = classWithConstructorDef | classDef | traitDef | objectDef

  def parse(t: P[Target], input: String): ParseResult[List[Target]] = {
    // e.g. class Person(arg: Name(f:String = "", l:String), age:Int,)
    parseAll(rep(t), input.replaceAll("([^,])\\s*\\)", "$1,)"))
  }

  def parse(input: String): ParseResult[List[Target]] = parse(allDef, input)

}
