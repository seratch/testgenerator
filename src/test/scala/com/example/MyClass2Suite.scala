package com.example

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class MyClass2Suite extends FunSuite with ShouldMatchers {

  test("available") {
    val instance = new MyClass2()
    instance should not be null
  }

}
