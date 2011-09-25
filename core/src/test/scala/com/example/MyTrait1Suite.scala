package com.example

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class MyTrait1Suite extends FunSuite with ShouldMatchers {

  type ? = this.type

  test("available") {
    val mixedin = new Object with MyTrait1
    mixedin should not be null
  }

}
