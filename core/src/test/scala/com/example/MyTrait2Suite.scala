package com.example

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class MyTrait2Suite extends FunSuite with ShouldMatchers {

  test("available") {
    val mixedin = new Object with MyTrait2
    mixedin should not be null
  }

}
