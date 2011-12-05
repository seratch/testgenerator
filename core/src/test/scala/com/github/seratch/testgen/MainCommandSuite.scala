package com.github.seratch.testgen

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class MainCommandSuite extends FunSuite with ShouldMatchers {

  type ? = this.type

  test("available") {
    MainCommand.isInstanceOf[Singleton] should equal(true)
  }

  test("specify -Dtestgen.testTemplate") {
    System.setProperty("testgen.testTemplate", "scalatest.Spec")
    System.setProperty("testgen.scalatest.Matchers", "MustMatchers")
    MainCommand.main(Array("com.github.seratch.testgen.MainCommand.scala"))
  }

  test("specify -testgen.testTemplate=scalatest.Spec as parameter") {
    System.setProperty("testgen.testTemplate", "xxx")
    System.setProperty("testgen.scalatest.Matchers", "xxx")
    System.setProperty("testgen.debug", "false")
    MainCommand.main(Array("com.github.seratch.testgen.MainCommand.scala"
      , "-testgen.testTemplate=scalatest.Spec"
      , "-testgen.scalatest.Matchers=MustMatchers"))
  }

}
