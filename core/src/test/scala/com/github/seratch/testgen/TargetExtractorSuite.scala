package com.github.seratch.testgen

import org.scalatest.matchers.ShouldMatchers
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite

@RunWith(classOf[JUnitRunner])
class TargetExtractorSuite extends FunSuite with ShouldMatchers {

  type ? = this.type

  val extractor = new TargetExtractor

  test("extract the list to import") {
    val result = extractor.extractImportList(
      "package com.exemple import hoge._ import java.io.{InputStream, OutputStream}"
    )
    println(result)
  }

  test("read the file and extact only the code which defines class/trait/object") {
    val lines = extractor.readLines("src/test/scala/com/example/noargs.scala")
    val result = extractor.extractDefOnly(lines)
    val expected = "package com.example\\s+" +
      "class MyClass1\\s+" +
      "class MyClass2\\s+" +
      "trait MyTrait1\\s+" +
      "trait MyTrait2\\s+" +
      "object MyObject1\\s+" +
      "object MyObject2\\s+"
    result.matches(expected) should equal(true)
  }

  test("extract all the targets from the target file") {
    val result = extractor.extract("src/test/scala/com/example/noargs.scala")
    result.size should be > 0
  }

}