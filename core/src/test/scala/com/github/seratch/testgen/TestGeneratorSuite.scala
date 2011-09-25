package com.github.seratch.testgen

import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class TestGeneratorSuite extends FunSuite with ShouldMatchers {

  type ? = this.type

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

}