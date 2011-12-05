package com.github.seratch.testgen

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class TestGenExecutorSuite extends FunSuite with ShouldMatchers {

  type ? = this.type // for IntelliJ IDEA

  test("available") {
    val config = new Config
    val instance = new TestGenExecutor(config)
    instance should not be null
  }

  test("execute") {
    val config = new Config
    val executor = new TestGenExecutor(config)
    val tests = executor.execute("com.github.seratch.testgen")
    tests.size should be(17)
  }

}
