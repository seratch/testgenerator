package com.example

import org.scalatest._
import org.scalatest.matchers._

class MyObject1Suite extends FunSuite with ShouldMatchers {

  type ? = this.type

  test("available") {
    MyObject1.isInstanceOf[Singleton] should equal(true)
  }

}
