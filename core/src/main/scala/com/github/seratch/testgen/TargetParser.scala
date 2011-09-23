package com.github.seratch.testgen

import util.parsing.combinator.JavaTokenParsers

case class TargetParser(fullPackageName: String, importList: List[String]) extends JavaTokenParsers {

  object white {
    val rep = "\\s+"
    val maybeRep = "\\s*"
  }

  type P[T] = Parser[T]

  def typeName: P[String] = white.maybeRep.r ~> ident <~ white.maybeRep.r

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
