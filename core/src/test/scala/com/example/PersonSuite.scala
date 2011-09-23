package com.example

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import com.example.bean.SampleBean

@RunWith(classOf[JUnitRunner])
class PersonSuite extends FunSuite with ShouldMatchers {

  type ? = this.type

  test("available") {
    val name: Name = null
    val instance = new Person(name)
    instance should not be null
  }

}
