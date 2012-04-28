package testgen

import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class TestGeneratorSuite extends FunSuite with ShouldMatchers {

  val config = new Config
  val generator = new TestGenerator(config)

  test("generate test to no arg target") {
    val config = new Config(
      srcDir = "src/test/scala"
    )
    val extractor = new TargetExtractor(config)
    val targets = extractor.extract("src/test/scala/com/example/noargs.scala")
    targets foreach {
      case target => {
        val test = generator.generate(target)
        test should not be null
      }
    }
  }

  test("generate built-in type values") {
    val target = new Target(
      defType = DefType.Class,
      fullPackageName = "com.example",
      typeName = "Sample",
      parameters = List(
        TargetParameter("intValue", "Int"),
        TargetParameter("shortValue", "Short"),
        TargetParameter("byteValue", "Byte"),
        TargetParameter("longValue", "Long"),
        TargetParameter("doubleValue", "Double"),
        TargetParameter("floatValue", "Float"),
        TargetParameter("booleanValue", "Boolean"),
        TargetParameter("charValue", "Char"),
        TargetParameter("stringValue", "String"),
        TargetParameter("seq", "Seq"),
        TargetParameter("list", "List"),
        TargetParameter("set", "Set"),
        TargetParameter("array", "Array"),
        TargetParameter("stream", "Stream"),
        TargetParameter("opt", "Option"),
        TargetParameter("map", "Map")
      )
    )
    val test = generator.generate(target)
    test should not be null
    test.sourceCode.contains("intValue: Int = 0") should be(true)
    test.sourceCode.contains("shortValue: Short = 0") should be(true)
    test.sourceCode.contains("byteValue: Byte = 0") should be(true)
    test.sourceCode.contains("longValue: Long = 0L") should be(true)
    test.sourceCode.contains("doubleValue: Double = 0D") should be(true)
    test.sourceCode.contains("booleanValue: Boolean = false") should be(true)
    test.sourceCode.contains("charValue: Char = ' '") should be(true)
    test.sourceCode.contains("seq: Seq[_] = Nil") should be(true)
    test.sourceCode.contains("list: List[_] = Nil") should be(true)
    test.sourceCode.contains("set: Set[_] = Set()") should be(true)
    test.sourceCode.contains("array: Array[_] = Array()") should be(true)
    test.sourceCode.contains("stream: Stream[_] = Stream()") should be(true)
    test.sourceCode.contains("opt: Option[_] = None") should be(true)
    test.sourceCode.contains("map: Map[_, _] = Map()") should be(true)
    test.sourceCode.contains("stringValue: String = \"\"") should be(true)
  }

  test("generate ScalaTestFunSuite") {
    val config = new Config(
      srcDir = "src/test/scala"
    )
    val generator = new TestGenerator(config)
    val test = generator.generate(new Target(
      defType = DefType.Class,
      fullPackageName = "com.example",
      typeName = "Sample"
    ))
    val expected = """package com.example

import org.scalatest._
import org.scalatest.matchers._

class SampleSuite extends FunSuite with ShouldMatchers {

  test("available") {
    val instance = new Sample()
    instance should not be null
  }

}
""".replaceAll("\r", "").replaceAll("\n", "\r\n")
    test.sourceCode should equal(expected)
  }

  test("generate ScalaTestFunSuite with JUnitRunner") {
    val config = new Config(
      srcDir = "src/test/scala",
      withJUnitRunner = true
    )
    val generator = new TestGenerator(config)
    val test = generator.generate(new Target(
      defType = DefType.Class,
      fullPackageName = "com.example",
      typeName = "Sample"
    ))
    val expected = """package com.example

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class SampleSuite extends FunSuite with ShouldMatchers {

  test("available") {
    val instance = new Sample()
    instance should not be null
  }

}
""".replaceAll("\r", "").replaceAll("\n", "\r\n")
    test.sourceCode should equal(expected)
  }

  test("generate ScalaTestFunSuite when name is LF") {
    val config = new Config(
      srcDir = "src/test/scala",
      lineBreak = LineBreak.LF
    )
    val generator = new TestGenerator(config)
    val test = generator.generate(new Target(
      defType = DefType.Class,
      fullPackageName = "com.example",
      typeName = "Sample"
    ))
    val expected = """package com.example

import org.scalatest._
import org.scalatest.matchers._

class SampleSuite extends FunSuite with ShouldMatchers {

  test("available") {
    val instance = new Sample()
    instance should not be null
  }

}
""".replaceAll("\r", "")
    test.sourceCode should equal(expected)
  }

  test("generate ScalaTestFunSuite with MustMatcher") {
    val config = new Config(
      srcDir = "src/test/scala",
      scalaTestMatchers = ScalaTestMatchers.Must
    )
    val generator = new TestGenerator(config)
    val test = generator.generate(new Target(
      defType = DefType.Class,
      fullPackageName = "com.example",
      typeName = "Sample"
    ))
    val expected = """package com.example

import org.scalatest._
import org.scalatest.matchers._

class SampleSuite extends FunSuite with MustMatchers {

  test("available") {
    val instance = new Sample()
    instance must not be null
  }

}
""".replaceAll("\r", "").replaceAll("\n", "\r\n")
    test.sourceCode should equal(expected)
  }

  test("generate ScalaTestFunSuite with MustMatcher for objects") {
    val config = new Config(
      srcDir = "src/test/scala",
      scalaTestMatchers = ScalaTestMatchers.Must
    )
    val generator = new TestGenerator(config)
    val test = generator.generate(new Target(
      defType = DefType.Object,
      fullPackageName = "com.example",
      typeName = "Sample"
    ))
    val expected = """package com.example

import org.scalatest._
import org.scalatest.matchers._

class SampleSuite extends FunSuite with MustMatchers {

  test("available") {
    val singleton = Sample
    singleton must not be null
  }

}
""".replaceAll("\r", "").replaceAll("\n", "\r\n")
    test.sourceCode should equal(expected)
  }

  test("generate ScalaTestFunSuite with MustMatcher for traits") {
    val config = new Config(
      srcDir = "src/test/scala",
      scalaTestMatchers = ScalaTestMatchers.Must
    )
    val generator = new TestGenerator(config)
    val test = generator.generate(new Target(
      defType = DefType.Trait,
      fullPackageName = "com.example",
      typeName = "Sample"
    ))
    val expected = """package com.example

import org.scalatest._
import org.scalatest.matchers._

class SampleSuite extends FunSuite with MustMatchers {

  test("available") {
    val mixedin = new Object with Sample
    mixedin must not be null
  }

}
""".replaceAll("\r", "").replaceAll("\n", "\r\n")
    test.sourceCode should equal(expected)
  }

  test("generate ScalaTestFunSuite without Matcher") {
    val config = new Config(
      srcDir = "src/test/scala",
      scalaTestMatchers = ScalaTestMatchers.None
    )
    val generator = new TestGenerator(config)
    val test = generator.generate(new Target(
      defType = DefType.Class,
      fullPackageName = "com.example",
      typeName = "Sample"
    ))
    val expected = """package com.example

import org.scalatest._
import org.scalatest.matchers._

class SampleSuite extends FunSuite {

  test("available") {
    val instance = new Sample()
    assert(instance != null)
  }

}
""".replaceAll("\r", "").replaceAll("\n", "\r\n")
    test.sourceCode should equal(expected)
  }

  test("generate ScalaTestAssertions") {
    val config = new Config(
      srcDir = "src/test/scala",
      testTemplate = TestTemplate.ScalaTestAssertions
    )
    val generator = new TestGenerator(config)
    val test = generator.generate(new Target(
      defType = DefType.Class,
      fullPackageName = "com.example",
      typeName = "Sample"
    ))
    val expected = """package com.example

import org.scalatest._
import org.scalatest.matchers._
import org.junit.Test

class SampleSuite extends Assertions with ShouldMatchers {

  @Test def available {
    val instance = new Sample()
    instance should not be null
  }

}
""".replaceAll("\r", "").replaceAll("\n", "\r\n")
    test.sourceCode should equal(expected)
  }

  test("generate ScalaTestSpec") {
    val config = new Config(
      srcDir = "src/test/scala",
      testTemplate = TestTemplate.ScalaTestSpec
    )
    val generator = new TestGenerator(config)
    val test = generator.generate(new Target(
      defType = DefType.Class,
      fullPackageName = "com.example",
      typeName = "Sample"
    ))
    val expected = """package com.example

import org.scalatest._
import org.scalatest.matchers._

class SampleSpec extends Spec with ShouldMatchers {

  describe("Sample") {
    it("should be available") {
      val instance = new Sample()
      instance should not be null
    }
  }

}
""".replaceAll("\r", "").replaceAll("\n", "\r\n")
    test.sourceCode should equal(expected)
  }

  test("generate ScalaTestWordSpec") {
    val config = new Config(
      srcDir = "src/test/scala",
      testTemplate = TestTemplate.ScalaTestWordSpec
    )
    val generator = new TestGenerator(config)
    val test = generator.generate(new Target(
      defType = DefType.Class,
      fullPackageName = "com.example",
      typeName = "Sample"
    ))
    val expected = """package com.example

import org.scalatest._
import org.scalatest.matchers._

class SampleSpec extends WordSpec with ShouldMatchers {

  "Sample" should {
    "be available" in {
      val instance = new Sample()
      instance should not be null
    }
  }

}
""".replaceAll("\r", "").replaceAll("\n", "\r\n")
    test.sourceCode should equal(expected)
  }

  test("generate ScalaTestFlatSpec") {
    val config = new Config(
      srcDir = "src/test/scala",
      testTemplate = TestTemplate.ScalaTestFlatSpec
    )
    val generator = new TestGenerator(config)
    val test = generator.generate(new Target(
      defType = DefType.Class,
      fullPackageName = "com.example",
      typeName = "Sample"
    ))
    val expected = """package com.example

import org.scalatest._
import org.scalatest.matchers._

class SampleSpec extends FlatSpec with ShouldMatchers {

  behavior of "Sample"

  it should "be available" in {
    val instance = new Sample()
    instance should not be null
  }

}
""".replaceAll("\r", "").replaceAll("\n", "\r\n")
    test.sourceCode should equal(expected)
  }

  test("generate ScalaTestFeatureSpec") {
    val config = new Config(
      srcDir = "src/test/scala",
      testTemplate = TestTemplate.ScalaTestFeatureSpec
    )
    val generator = new TestGenerator(config)
    val test = generator.generate(new Target(
      defType = DefType.Class,
      fullPackageName = "com.example",
      typeName = "Sample"
    ))
    val expected = """package com.example

import org.scalatest._
import org.scalatest.matchers._

class SampleSpec extends FeatureSpec with ShouldMatchers {

  feature("Sample") {
    scenario("it is prepared") {
      val instance = new Sample()
      instance should not be null
    }
  }

}
""".replaceAll("\r", "").replaceAll("\n", "\r\n")
    test.sourceCode should equal(expected)
  }

  test("generate SpecsSpecification") {
    val config = new Config(
      srcDir = "src/test/scala",
      testTemplate = TestTemplate.SpecsSpecification
    )
    val generator = new TestGenerator(config)
    val test = generator.generate(new Target(
      defType = DefType.Class,
      fullPackageName = "com.example",
      typeName = "Sample"
    ))
    val expected = """package com.example

import org.specs.Specification

class SampleSpec extends Specification {

  "Sample" should {
    "be available" in {
      val instance = new Sample()
      instance must notBeNull
    }
  }

}
""".replaceAll("\r", "").replaceAll("\n", "\r\n")
    test.sourceCode should equal(expected)
  }

  test("generate Specs2Specification") {
    val config = new Config(
      srcDir = "src/test/scala",
      testTemplate = TestTemplate.Specs2Specification
    )
    val generator = new TestGenerator(config)
    val test = generator.generate(new Target(
      defType = DefType.Class,
      fullPackageName = "com.example",
      typeName = "Sample"
    ))
    val expected = """package com.example

import org.specs2.mutable.Specification

class SampleSpec extends Specification {

  "Sample" should {
    "be available" in {
      val instance = new Sample()
      instance must not beNull
    }
  }

}
""".replaceAll("\r", "").replaceAll("\n", "\r\n")
    test.sourceCode should equal(expected)
  }

}
