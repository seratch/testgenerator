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

  // --- main ---

  def allDef = {
    (prefixOfClass ~> classWithConstructorDef <~ suffixOfClass) |
      (prefixOfClass ~> classDef <~ suffixOfClass) |
      (prefixOfObject ~> objectDef <~ suffixOfObject) |
      (prefixOfTrait ~> traitDef <~ suffixOfTrait)
  }

  def parse(t: P[Target], input: String): ParseResult[List[Target]] = {
    // e.g. class Person(arg: Name(f:String = "", l:String), age: Bean = new Bean(,),)
    val replacedInput = input.replaceAll("\\(\\s*\\)", "(,)").replaceAll("([^,])\\s*\\)", "$1,)")
    parseAll(rep(t), replacedInput)
  }

  def parse(input: String): ParseResult[List[Target]] = parse(allDef, input)

  // --- basic ---

  def variableName = "\\w+".r

  def packageName = "[\\w\\.]+".r

  def literal = {
    // stringLiteral does not work for string contains backslash, etc..
    "'.{1}'".r | "\"[^(\")]+\"".r | stringLiteral | packageName
  }

  // --- type def ---

  def typeName = (variableName <~ typeParametersName) | variableName

  def typeParametersName: P[Any] = {
    "[" <~ rep(variableName | ">:" | "<:" | "+" | "-" | "," | typeParametersName) <~ "]"
  }

  // --- constructor and args ---

  def newLiteral: P[Any] = {
    (rep(variableName) ~ "(" ~ argsInNewLiteral ~ ")")
  }

  def argsInNewLiteral = {
    // e.g. new Something() is converted to new Something(,)
    rep("," | ((newLiteral | literal) ~ ",") | newLiteral | literal)
  }

  def args = {

    def argDefaultValue = newLiteral | literal

    def valDef = "val\\s".r

    def varDef = "var\\s".r

    def  annotationOrValOrVar = rep(annotationValue | valDef | varDef )

    def argsWithDefaultValue = {
      annotationOrValOrVar ~> variableName ~ ":" ~ typeName <~ "=" <~ argDefaultValue <~ ","
    }

    def argsWithoutDefaultValue = {
      annotationOrValOrVar ~> variableName ~ ":" ~ typeName <~ ","
    }

    rep(argsWithDefaultValue | argsWithoutDefaultValue) ^^ {
      case argList => argList map {
        case name ~ ":" ~ typeName => (name, typeName)
      }
    }
  }

  // --- modifier ---

  def annotationValue = {

    def noArg = {
      "@" ~ packageName
    }

    def valueOnly = {
      "@" ~ packageName ~ "(" ~ argsInNewLiteral ~ ")"
    }

    def keyAndValue = {
      "@" ~ packageName ~ "(" ~ rep("," | (packageName ~ "=" ~ (newLiteral | literal))) ~ ")"
    }

    keyAndValue | valueOnly | noArg
  }

  def finalDef = "final"

  def caseDef = "case"

  def packagePrivateDef = "private[" ~> packageName ~> "]"

  def protectedDef = "protected"

  def extendsDef = "extends" ~ packageName

  def withDef = rep("with" ~ packageName)

  // --- class ---

  def prefixOfClass = {
    rep(annotationValue | packagePrivateDef | protectedDef | caseDef | finalDef)
  }

  def suffixOfClass = {
    rep(typeParametersName | (extendsDef ~ withDef) | extendsDef)
  }

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

  // --- object ---

  def prefixOfObject = {
    rep(annotationValue | packagePrivateDef | protectedDef | caseDef | finalDef)
  }

  def suffixOfObject = {
    rep((extendsDef ~ withDef) | extendsDef)
  }

  def objectDef = "object" ~> typeName ^^ {
    name => new Target(
      fullPackageName = fullPackageName,
      importList = importList,
      defType = DefType.Object,
      typeName = name
    )
  }

  // --- trait ---

  def prefixOfTrait = {
    rep(annotationValue | packagePrivateDef | protectedDef)
  }

  def suffixOfTrait = {
    rep(typeParametersName | (extendsDef ~ withDef) | extendsDef)
  }

  def traitDef = "trait" ~> typeName ^^ {
    name => new Target(
      fullPackageName = fullPackageName,
      importList = importList,
      defType = DefType.Trait,
      typeName = name
    )
  }

}
