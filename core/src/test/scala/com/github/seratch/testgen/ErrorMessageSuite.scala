package com.github.seratch.testgen

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ErrorMessageSuite extends FunSuite with ShouldMatchers {

  type ? = this.type // for IntelliJ IDEA

  test("available") {
    ErrorMessage.isInstanceOf[Singleton] should equal(true)
  }

  test("noTargetsToGenerateFor") {
     val msg = ErrorMessage.noTargetsToGenerateFor("com.github")
    msg should be("Cannot find the targets to generate test for \"com.github\"")
   }

}
