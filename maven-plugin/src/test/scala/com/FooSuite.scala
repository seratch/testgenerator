package com

import org.scalatest._
import org.scalatest.matchers._

class FooSuite extends FunSuite with ShouldMatchers {

  test("available") {
    val instance = new Foo()
    instance should not be null
  }

}
