package com.github.seratch.testgen

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers

@RunWith(classOf[JUnitRunner])
class TestSuite extends FunSuite with ShouldMatchers {

  type ? = this.type

  val extractor = new TargetExtractor
  val generator = new TestGenerator

  test("createIfNotExist") {
    val targets = extractor.extract("src/test/scala/com/example/noargs.scala")
    targets foreach {
      case target => {
        val test = generator.generate(target)
        test.createFileIfNotExist()
      }
    }
  }

}