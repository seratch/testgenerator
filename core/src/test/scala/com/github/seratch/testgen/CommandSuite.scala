package com.github.seratch.testgen

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class CommandSuite extends FunSuite with ShouldMatchers {

  type ? = this.type

  test("available") {
    Command.isInstanceOf[Singleton] should equal(true)
  }

  test("specify -Dtestgen.testTemplate") {
    System.setProperty("testgen.testTemplate", "scalatest.Spec")
    System.setProperty("testgen.scalatest.Matchers", "MustMatchers")
    Command.main(Array("com.github.seratch.testgen.Command.scala"))
  }

}
