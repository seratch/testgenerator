package com.example

import org.scalatest._
import org.scalatest.matchers._

class MyClass1Suite extends FunSuite with ShouldMatchers {

  type ? = this.type

  test("available") {
    val instance = new MyClass1()
    instance should not be null
  }

}
