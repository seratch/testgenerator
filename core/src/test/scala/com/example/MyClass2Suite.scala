package com.example

import org.scalatest._
import org.scalatest.matchers._

class MyClass2Suite extends FunSuite with ShouldMatchers {

  type ? = this.type

  test("available") {
    val instance = new MyClass2()
    instance should not be null
  }

}
