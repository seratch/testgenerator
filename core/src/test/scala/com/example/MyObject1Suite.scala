package com.example

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class MyObject1Suite extends FunSuite with ShouldMatchers {

  test("available") {
    MyObject1.isInstanceOf[Singleton] should equal(true)
  }

}
