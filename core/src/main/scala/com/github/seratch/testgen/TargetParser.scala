package com.github.seratch.testgen

import util.parsing.combinator.JavaTokenParsers

case class TargetParser(fullPackageName: String) extends JavaTokenParsers {

  object white {
    val rep = "\\s+"
    val maybeRep = "\\s*"
  }

  type P[T] = Parser[T]

  def typeName: P[String] = white.maybeRep.r ~> ident <~ white.maybeRep.r

  def classDef = ("class" + white.rep).r ~> typeName ^^ {
    name => new Target(
      fullPackageName = fullPackageName,
      defType = DefType.Class,
      typeName = name
    )
  }

  def traitDef = ("trait" + white.rep).r ~> typeName ^^ {
    name => new Target(
      fullPackageName = fullPackageName,
      defType = DefType.Trait,
      typeName = name
    )
  }

  def objectDef = ("object" + white.rep).r ~> typeName ^^ {
    name => new Target(
      fullPackageName = fullPackageName,
      defType = DefType.Object,
      typeName = name
    )
  }

  def allDef = classDef | traitDef | objectDef

  def parse(t: P[Target], input: String): ParseResult[List[Target]] = parseAll(rep(t), input)

  def parse(input: String): ParseResult[List[Target]] = parseAll(rep(allDef), input)

}
