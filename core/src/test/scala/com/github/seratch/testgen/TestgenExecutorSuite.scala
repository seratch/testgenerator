package com.github.seratch.testgen

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class TestgenExecutorSuite extends FunSuite with ShouldMatchers {

  type ? = this.type // for IntelliJ IDEA

  test("available") {
    val config = new Config
    val instance = new TestgenExecutor(config)
    instance should not be null
  }

  test("generateTests") {
    val config = new Config
    val executor = new TestgenExecutor(config)
    val tests = executor.generateTests("com.github.seratch.testgen")
    tests.size should be(17)
  }

}