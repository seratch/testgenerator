package com.github.seratch.testgen

case class DefType(defType: String)

object DefType {
  val Object = DefType("object")
  val Class = DefType("class")
  val Trait = DefType("trait")
}
