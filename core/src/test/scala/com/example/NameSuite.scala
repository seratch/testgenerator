package com.example

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import com.example.bean.SampleBean

@RunWith(classOf[JUnitRunner])
class NameSuite extends FunSuite with ShouldMatchers {

  type ? = this.type

  test("available") {
    val first: String = null
    val last: String = null
    val instance = new Name(first, last)
    instance should not be null
  }

}
