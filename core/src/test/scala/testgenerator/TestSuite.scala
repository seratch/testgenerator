package testgenerator

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import java.io.File

@RunWith(classOf[JUnitRunner])
class TestSuite extends FunSuite with ShouldMatchers {

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

  test("createIfNotExist (IO)") {
    val config = new Config(
      srcDir = "src/test/scala"
    )
    val extractor = new TargetExtractor(config)
    val targets = extractor.extract("src/test/scala/example/IO.scala")
    targets foreach {
      case target => {
        val test = generator.generate(target)
        test.createFileIfNotExist()
      }
    }
  }

  test("createIfNotExist (Scope)") {
    val config = new Config(
      srcDir = "src/test/scala"
    )
    val extractor = new TargetExtractor(config)
    val targets = extractor.extract("src/test/scala/example/Scope.scala")
    targets foreach {
      case target => {
        val test = generator.generate(target)
        test.createFileIfNotExist()
      }
    }
  }

  test("createIfNotExist (Case)") {
    val config = new Config(
      srcDir = "src/test/scala"
    )
    val extractor = new TargetExtractor(config)
    val targets = extractor.extract("src/test/scala/example/Case.scala")
    targets foreach {
      case target => {
        val test = generator.generate(target)
        test.createFileIfNotExist()
      }
    }
  }

  test("createIfNotExist (Final)") {
    val config = new Config(
      srcDir = "src/test/scala"
    )
    val extractor = new TargetExtractor(config)
    val targets = extractor.extract("src/test/scala/example/Final.scala")
    targets foreach {
      case target => {
        val test = generator.generate(target)
        test.createFileIfNotExist()
      }
    }
  }

  test("createIfNotExist (Extends)") {
    val config = new Config(
      srcDir = "src/test/scala"
    )
    val extractor = new TargetExtractor(config)
    val targets = extractor.extract("src/test/scala/example/Extends.scala")
    targets foreach {
      case target => {
        val test = generator.generate(target)
        test.createFileIfNotExist()
      }
    }
  }

  test("createIfNotExist (Companion)") {
    val config = new Config(
      srcDir = "src/test/scala"
    )
    val extractor = new TargetExtractor(config)
    val targets = extractor.extract("src/test/scala/example/Companion.scala")
    targets foreach {
      case target => {
        val test = generator.generate(target)
        test.createFileIfNotExist()
      }
    }
  }

  test("createIfNotExist (Annotation)") {
    val config = new Config(
      srcDir = "src/test/scala"
    )
    val extractor = new TargetExtractor(config)
    val targets = extractor.extract("src/test/scala/example/Annotation.scala")
    targets foreach {
      case target => {
        val test = generator.generate(target)
        test.createFileIfNotExist()
      }
    }
  }

  test("createIfNotExist (Semicolon)") {
    val config = new Config(
      srcDir = "src/test/scala"
    )
    val extractor = new TargetExtractor(config)
    val targets = extractor.extract("src/test/scala/example/Semicolon.scala")
    targets foreach {
      case target => {
        val test = generator.generate(target)
        test.createFileIfNotExist()
      }
    }
  }

  test("createIfNotExist (WithTypeParameters)") {
    val config = new Config(
      srcDir = "src/test/scala"
    )
    val extractor = new TargetExtractor(config)
    val targets = extractor.extract("src/test/scala/example/typeparameters/WithTypeParameters.scala")
    targets foreach {
      case target => {
        val test = generator.generate(target)
        test.createFileIfNotExist()
      }
    }
  }

  test("createIfNotExist (ScalaTestOrSpecs/ScalaTestAssertions)") {
    val config = new Config(
      srcDir = "src/test/scala",
      testTemplate = TestTemplate.ScalaTestAssertions
    )
    val extractor = new TargetExtractor(config)
    val generator = new TestGenerator(config)
    val targets = extractor.extract("src/test/scala/example/ScalaTestOrSpecs.scala")
    targets foreach {
      case target => generator.generate(target).createFileIfNotExist()
    }
  }

  test("createIfNotExist (ScalaTestOrSpecs/ScalaTestFeatureSpec)") {
    val config = new Config(
      srcDir = "src/test/scala",
      testTemplate = TestTemplate.ScalaTestFeatureSpec
    )
    val extractor = new TargetExtractor(config)
    val generator = new TestGenerator(config)
    val targets = extractor.extract("src/test/scala/example/ScalaTestOrSpecs.scala")
    targets foreach {
      case target => generator.generate(target).createFileIfNotExist()
    }
  }

  test("createIfNotExist (ScalaTestOrSpecs/ScalaTestFlatSpec)") {
    val config = new Config(
      srcDir = "src/test/scala",
      testTemplate = TestTemplate.ScalaTestFlatSpec
    )
    val extractor = new TargetExtractor(config)
    val generator = new TestGenerator(config)
    val targets = extractor.extract("src/test/scala/example/ScalaTestOrSpecs.scala")
    targets foreach {
      case target => generator.generate(target).createFileIfNotExist()
    }
  }

  test("createIfNotExist (ScalaTestOrSpecs/ScalaTestFunSuite)") {
    val config = new Config(
      srcDir = "src/test/scala",
      testTemplate = TestTemplate.ScalaTestFunSuite
    )
    val extractor = new TargetExtractor(config)
    val generator = new TestGenerator(config)
    val targets = extractor.extract("src/test/scala/example/ScalaTestOrSpecs.scala")
    targets foreach {
      case target => generator.generate(target).createFileIfNotExist()
    }
  }

  test("createIfNotExist (ScalaTestOrSpecs/ScalaTestSpec)") {
    val config = new Config(
      srcDir = "src/test/scala",
      testTemplate = TestTemplate.ScalaTestSpec
    )
    val extractor = new TargetExtractor(config)
    val generator = new TestGenerator(config)
    val targets = extractor.extract("src/test/scala/example/ScalaTestOrSpecs.scala")
    targets foreach {
      case target => generator.generate(target).createFileIfNotExist()
    }
  }

  test("createIfNotExist (ScalaTestOrSpecs/ScalaTestWordSpec)") {
    val config = new Config(
      srcDir = "src/test/scala",
      testTemplate = TestTemplate.ScalaTestWordSpec
    )
    val extractor = new TargetExtractor(config)
    val generator = new TestGenerator(config)
    val targets = extractor.extract("src/test/scala/example/ScalaTestOrSpecs.scala")
    targets foreach {
      case target => generator.generate(target).createFileIfNotExist()
    }
  }

  test("createIfNotExist (ScalaTestOrSpecs/SpecsSpecification)") {
    val config = new Config(
      srcDir = "src/test/scala",
      testTemplate = TestTemplate.SpecsSpecification
    )
    val extractor = new TargetExtractor(config)
    val generator = new TestGenerator(config)
    val targets = extractor.extract("src/test/scala/example/ScalaTestOrSpecs.scala")
    targets foreach {
      case target => generator.generate(target).createFileIfNotExist()
    }
  }

  test("createIfNotExist (ScalaTestOrSpecs/Specs2Specification)") {
    val config = new Config(
      srcDir = "src/test/scala",
      testTemplate = TestTemplate.Specs2Specification
    )
    val extractor = new TargetExtractor(config)
    val generator = new TestGenerator(config)
    val targets = extractor.extract("src/test/scala/example/ScalaTestOrSpecs.scala")
    targets foreach {
      case target => generator.generate(target).createFileIfNotExist()
    }
  }

}