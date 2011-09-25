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

  def packageName = "[\\w\\.]+".r

  def annotationValue = "@" ~ packageName

  def typeParametersName: P[Any] = "[" <~ rep(variableName | ">:" | "<:" | "+" | "-" | "," | typeParametersName) <~ "]"

  def typeName = (variableName <~ typeParametersName) | variableName

  def argsWithDefaultValue = rep(annotationValue | "val " | "var ") ~> variableName ~ ":" ~ typeName <~ "=" <~ argDefaultValue <~ ","

  def argsWithoutDefaultValue = rep(annotationValue | "val " | "var ") ~> variableName ~ ":" ~ typeName <~ ","

  def args = rep(argsWithDefaultValue | argsWithoutDefaultValue) ^^ {
    case argList => argList map {
      case name ~ ":" ~ typeName => (name, typeName)
    }
  }

  def literal = "[\\w\"']+".r

  def newLiteral: P[Any] = {
    (rep(variableName) ~ "(" ~ argsInNewLiteral ~ ")")
  }

  def argsInNewLiteral = {
    // e.g. new Something() is converted to new Something(,)
    rep("," | ((newLiteral | literal) ~ ",") | newLiteral | literal)
  }

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

  def importDef = "import" ~> packageName ^^ {
    name => new Target(
      fullPackageName = fullPackageName,
      defType = DefType.Import,
      typeName = name
    )
  }

  def finalDef = "final"

  def caseDef = "case"

  def packagePrivateDef = "private[" ~> packageName ~> "]"

  def protectedDef = "protected"

  def prefixOfClass = rep(annotationValue | packagePrivateDef | protectedDef | caseDef | finalDef)

  def suffixOfClass = rep(typeParametersName)

  def prefixOfObject = rep(annotationValue | packagePrivateDef | protectedDef | caseDef | finalDef)

  def prefixOfTrait = rep(annotationValue | packagePrivateDef | protectedDef)

  def suffixOfTrait = rep(typeParametersName)

  def allDef = {
    importDef |
      (prefixOfClass ~> classWithConstructorDef <~ suffixOfClass) |
      (prefixOfClass ~> classDef <~ suffixOfClass) |
      (prefixOfObject ~> objectDef) |
      (prefixOfTrait ~> traitDef <~ suffixOfTrait)
  }

  def parse(t: P[Target], input: String): ParseResult[List[Target]] = {
    // e.g. class Person(arg: Name(f:String = "", l:String), age: Bean = new Bean(,),)
    val replacedInput = input.replaceAll("\\(\\s*\\)", "(,)").replaceAll("([^,])\\s*\\)", "$1,)")
    parseAll(rep(t), replacedInput)
  }

  def parse(input: String): ParseResult[List[Target]] = parse(allDef, input)

}
