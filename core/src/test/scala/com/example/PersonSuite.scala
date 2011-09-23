package com.example

import org.scalatest._
import org.scalatest.matchers._
import com.example.bean.SampleBean

class PersonSuite extends FunSuite with ShouldMatchers {

  type ? = this.type

  test("available") {
    val name: Name = null
    val instance = new Person(name)
    instance should not be null
  }

}
