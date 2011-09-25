package com.github.seratch.testgen

import org.scalatest.matchers.ShouldMatchers
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite

@RunWith(classOf[JUnitRunner])
class TargetExtractorSuite extends FunSuite with ShouldMatchers {

  type ? = this.type

  val config = new Config
  val extractor = new TargetExtractor(config)

  test("extract package") {
    val targets = extractor.extract("com.github.seratch")
    targets.size should be > 0
  }

  test("extract classname") {
    val targets = extractor.extract("com.github.seratch.testgen.Command")
    targets.size should equal(1)
  }

  test("extract path") {
    {
      val targets = extractor.extract("com/github/seratch/testgen/Command.scala")
      targets.size should equal(1)
    }
    {
      val targets = extractor.extract("com/github/seratch/testgen/Command")
      targets.size should equal(1)
    }
    {
      val targets = extractor.extract("src/main/scala/com/github/seratch/testgen/Command.scala")
      targets.size should equal(1)
    }
  }

  test("extract directory") {
    val targets = extractor.extract("src/main/scala/com/github/seratch")
    targets.size should be > 0
  }

  test("extract the list to import") {
    val result = extractor.extractImportList(
      "package com.exemple import hoge._ import java.io.{InputStream, OutputStream}"
    )
    result.size should equal(2)
    result(0) should equal("hoge._")
    result(1) should equal("java.io._")
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

  test("extract only the code which defines class/trait/object") {
    {
      val lines = List(
        "package com.example ",
        "import com.example.util._",
        "",
        "class Sample(name: String = \"\") {",
        "  def doSomething() = println(\"foo\")",
        "}",
        "// this line is commented out",
        "/** several line comment",
        "/* foo,var,baz",
        " */ class Sample2 {",
        "  def doSomething() = println(\"foo\")",
        "}",
        "class Sample3 { }"
      )
      val result = extractor.extractDefOnly(lines)
      val expected = "package com.example\\s+" +
        "import com.example.util._\\s+" +
        "class Sample\\(name: String = \"\"\\)\\s+" +
        "class Sample2\\s+" +
        "class Sample3\\s+"
      result.matches(expected) should equal(true)
    }
    {
      val lines = List(
        "package com{ ",
        "package example {",
        "  import com.example.util._",
        "  class Sample(name: String = \"\") {",
        "    def doSomething() = println(\"foo\")",
        "  }",
        "  // this line is commented out",
        "  /** several line comment",
        "  /* foo,var,baz",
        "   */ class Sample2 {",
        "    def doSomething() = println(\"foo\")",
        "    }",
        "  }",
        "}"
      )
      val result = extractor.extractDefOnly(lines)
      // TODO currently nested package is not supported
      val expected = "package com\\s+"
      result.matches(expected) should equal(true)
    }
  }

  test("extract all the targets from the code which defines class/trait/object") {
    val defOnly = "package com.example " +
      "trait Example " +
      "package com.github " +
      "object GitHub " +
      "class WithArgs(name: String = \"foo\", age: Int)"
    val result = extractor.extractFromDefOnly(defOnly)
    result.size should equal(3)
    result(0).fullPackageName should equal("com.example")
    result(0).typeName should equal("Example")
    result(0).defType should equal(DefType.Trait)
    result(0).parameters should equal(Nil)
    result(1).fullPackageName should equal("com.github")
    result(1).typeName should equal("GitHub")
    result(1).defType should equal(DefType.Object)
    result(1).parameters should equal(Nil)
    result(2).fullPackageName should equal("com.github")
    result(2).typeName should equal("WithArgs")
    result(2).defType should equal(DefType.Class)
    result(2).parameters should equal(List(TargetParameter("name", "String"), TargetParameter("age", "Int")))
  }

  test("extract all the targets from the target file") {
    val config = new Config(
      srcDir = "src/test/scala"
    )
    val extractor = new TargetExtractor(config)
    val result = extractor.extract("src/test/scala/com/example/noargs.scala")
    result.size should equal(6)
    result(0).fullPackageName should equal("com.example")
    result(0).typeName should equal("MyClass1")
    result(0).defType should equal(DefType.Class)
    result(1).fullPackageName should equal("com.example")
    result(1).typeName should equal("MyClass2")
    result(1).defType should equal(DefType.Class)
    result(2).fullPackageName should equal("com.example")
    result(2).typeName should equal("MyTrait1")
    result(2).defType should equal(DefType.Trait)
    result(3).fullPackageName should equal("com.example")
    result(3).typeName should equal("MyTrait2")
    result(3).defType should equal(DefType.Trait)
    result(4).fullPackageName should equal("com.example")
    result(4).typeName should equal("MyObject1")
    result(4).defType should equal(DefType.Object)
    result(5).fullPackageName should equal("com.example")
    result(5).typeName should equal("MyObject2")
    result(5).defType should equal(DefType.Object)
  }

}