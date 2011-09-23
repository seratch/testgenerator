package com.example

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class MyObject2Suite extends FunSuite with ShouldMatchers {

  type ? = this.type

  test("available") {
    MyObject2.isInstanceOf[Singleton] should equal(true)
  }

}
