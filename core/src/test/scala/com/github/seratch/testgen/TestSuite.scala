package com.github.seratch.testgen

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers

@RunWith(classOf[JUnitRunner])
class TestSuite extends FunSuite with ShouldMatchers {

  type ? = this.type

  val config = new Config
  val extractor = new TargetExtractor(config)
  val generator = new TestGenerator(config)

  test("createIfNotExist (no arg)") {
    val targets = extractor.extract("src/test/scala/com/example/noargs.scala")
    targets foreach {
      case target => {
        val test = generator.generate(target)
        test.createFileIfNotExist()
      }
    }
  }

  test("createIfNotExist (with args)") {
    val targets = extractor.extract("src/test/scala/com/example/withargs.scala")
    targets foreach {
      case target => {
        val test = generator.generate(target)
        test.createFileIfNotExist()
      }
    }
  }

}