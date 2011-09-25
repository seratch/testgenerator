package com.github.seratch.testgen

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import java.io.File

@RunWith(classOf[JUnitRunner])
class TestSuite extends FunSuite with ShouldMatchers {

  type ? = this.type

  val config = new Config
  val generator = new TestGenerator(config)

  test("createIfNotExist (no arg)") {
    val config = new Config(
      srcDir = "src/test/scala"
    )
    val extractor = new TargetExtractor(config)
    val targets = extractor.extract("src/test/scala/com/example/noargs.scala")
    targets foreach {
      case target => {
        val test = generator.generate(target)
        test.createFileIfNotExist()
      }
    }
  }

  test("createIfNotExist depth") {
    val config = new Config(
      srcDir = "src/test/scala"
    )
    val extractor = new TargetExtractor(config)
    val targets = extractor.extract("src/test/scala/example/depthtest.scala")
    targets foreach {
      case target => {
        val test = generator.generate(target)
        test.createFileIfNotExist()
      }
    }
    new File("src/test/scala/example/depth/depth/SampleSuite.scala").delete()
    new File("src/test/scala/example/depth/depth/").delete()
    new File("src/test/scala/example/depth/").delete()
  }

  test("createIfNotExist (with args)") {
    val config = new Config(
      srcDir = "src/test/scala"
    )
    val extractor = new TargetExtractor(config)
    val targets = extractor.extract("src/test/scala/com/example/withargs.scala")
    targets foreach {
      case target => {
        val test = generator.generate(target)
        test.createFileIfNotExist()
      }
    }
  }

  test("createIfNotExist (Log)") {
    val config = new Config(
      srcDir = "src/test/scala"
    )
    val extractor = new TargetExtractor(config)
    val targets = extractor.extract("src/test/scala/example/Log.scala")
    targets foreach {
      case target => {
        val test = generator.generate(target)
        test.createFileIfNotExist()
      }
    }
  }

  test("extract case class") {
    val defOnly = "package example    class Logger     case class Log(logger:Logger)"
    val extractor = new TargetExtractor(config)
    val targets = extractor.extractFromDefOnly(defOnly)
    targets.size should equal(2)
    targets(0).typeName should equal("Logger")
    targets(1).typeName should equal("Log")
  }

}