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
    val targets = extractor.extract("com.github.seratch.testgen.MainCommand")
    targets.size should equal(1)
  }

  test("extract path") {
    {
      val targets = extractor.extract("com/github/seratch/testgen/MainCommand.scala")
      targets.size should equal(1)
    }
    {
      val targets = extractor.extract("com/github/seratch/testgen/MainCommand")
      targets.size should equal(1)
    }
    {
      val targets = extractor.extract("src/main/scala/com/github/seratch/testgen/MainCommand.scala")
      targets.size should equal(1)
    }
  }

  test("extract directory") {
    val targets = extractor.extract("src/main/scala/com/github/seratch")
    targets.size should be > 0
  }

  test("read the file and extact only the code which defines class/trait/object (=defOnly)") {
    val lines = extractor.readLines("src/test/scala/com/example/noargs.scala")
    val (result, importedList) = extractor.getDefOnlyAndImportedList(lines)
    val expected = "package com.example\\s+" +
      "class MyClass1\\s+" +
      "class MyClass2\\s+" +
      "trait MyTrait1\\s+" +
      "trait MyTrait2\\s+" +
      "object MyObject1\\s+" +
      "object MyObject2\\s+"
    result.matches(expected) should equal(true)
  }

  test("extract from the code which contains string literal") {
    {
      val lines = List(
        "package com.example",
        "import java.io._ ",
        "case class MyClass1(v: String = \" this is a string literal. package string.literal class MyClassX \")",
        "case class MyClass2(v: String)"
      )
      val (result, importList) = extractor.getDefOnlyAndImportedList(lines)
      val expected = "package com.example\\s+" +
        "case class MyClass1\\(v: String = \"\"\\)\\s+" +
        "case class MyClass2\\(v: String\\)\\s*"
      result.matches(expected) should equal(true)
      importList.size should equal(1)
    }
    {
      val lines = List(
        "package com.example",
        "import java.io._ ",
        "case class MyClass1(v: String = \" this is a string literal. \\\"package\\\" string.literal class MyClassX \")",
        "case class MyClass2(v: String)"
      )
      val (result, importList) = extractor.getDefOnlyAndImportedList(lines)
      val expected = "package com.example\\s+" +
        "case class MyClass1\\(v: String = \"\"\\)\\s+" +
        "case class MyClass2\\(v: String\\)\\s*"
      result.matches(expected) should equal(true)
      importList.size should equal(1)
    }
  }

  test("extract from the code which contains here document") {
    {
      val lines = List(
        "package com.example",
        "import java.io._ ",
        "case class MyClass1(v: String = \"\"\" this is a string literal. package string.literal class MyClassX \"\"\")",
        "case class MyClass2(v: String)"
      )
      val (result, importList) = extractor.getDefOnlyAndImportedList(lines)
      val expected = "package com.example\\s+" +
        "case class MyClass1\\(v: String = \"\"\\)\\s+" +
        "case class MyClass2\\(v: String\\)\\s*"
      result.matches(expected) should equal(true)
      importList.size should equal(1)
    }
  }

  test("extract case class in several lines") {
    val lines = List(
      "case class HttpResponse(val statusCode: Int, ",
      "  val headers: Map[String, List[String]],",
      "  val content: String)"
    )
    val (result, _) = extractor.getDefOnlyAndImportedList(lines)
    val expected = "case class HttpResponse\\(val statusCode: Int,\\s+" +
      "val headers: Map\\[String, List\\[String\\]\\],\\s+" +
      "val content: String\\)"
    result.matches(expected) should equal(true)
  }

  test("extract only the code which defines class/trait/object (=defOnly)") {
    {
      val lines = List(
        "package com.example ",
        "import com.example.util.{IOUtil, StringUtil}",
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
      val (defOnly, importedList) = extractor.getDefOnlyAndImportedList(lines)
      val expected = "package com.example\\s+" +
        "\\s+" +
        "class Sample\\(name: String = \"\"\\)\\s+" +
        "class Sample2\\s+" +
        "class Sample3\\s+"
      defOnly.matches(expected) should equal(true)
      importedList should equal(List(
        "com.example.util._"
      ))
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
      val (result, importedList) = extractor.getDefOnlyAndImportedList(lines)
      // TODO currently nested package is not supported
      val expected = "package com\\s+"
      result.matches(expected) should equal(true)
    }
  }

  test("extract all the targets from defOnly") {
    {
      val lines = List(
        "package com.github.seratch.testgen",
        "case class Config(encoding: String = \"UTF-8\",",
        "srcDir: String = \"src/main/scala\",",
        "srcTestDir: String = \"src/test/scala\",",
        "testTemplate: TestTemplate = TestTemplate.ScalaTestFunSuite,",
        "scalaTestMatchers: ScalaTestMatchers = ScalaTestMatchers.Should)"
      )
      val (defOnly, importedList) = extractor.getDefOnlyAndImportedList(lines)
      val targets = extractor.extractFromDefOnly(defOnly, importedList)
      targets.size should equal(1)
    }
    {
      val defOnly = "package com.example " +
        "trait Example " +
        "package com.github " +
        "object GitHub " +
        "class WithArgs(name: String = \"foo\", age: Int)"
      val result = extractor.extractFromDefOnly(defOnly, Nil)
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

  test("extract case class from defOnly") {
    val defOnly = "package example    class Logger     case class DebugLog(logger:Logger)"
    val extractor = new TargetExtractor(config)
    val targets = extractor.extractFromDefOnly(defOnly, Nil)
    targets.size should equal(2)
    targets(0).typeName should equal("Logger")
    targets(1).typeName should equal("DebugLog")
  }

  test("extract case object from defOnly") {
    val defOnly = "package example    case object CaseObject"
    val extractor = new TargetExtractor(config)
    val targets = extractor.extractFromDefOnly(defOnly, Nil)
    targets.size should equal(1)
    targets(0).typeName should equal("CaseObject")
  }

  test("extract final class from defOnly") {
    val defOnly = "package example    final class Final"
    val extractor = new TargetExtractor(config)
    val targets = extractor.extractFromDefOnly(defOnly, Nil)
    targets.size should equal(1)
    targets(0).typeName should equal("Final")
  }

  test("extract final object from defOnly") {
    val defOnly = "package example    final object Final"
    val extractor = new TargetExtractor(config)
    val targets = extractor.extractFromDefOnly(defOnly, Nil)
    targets.size should equal(1)
    targets(0).typeName should equal("Final")
  }

  test("extract package private class/trait/object from defOnly") {
    {
      val defOnly = "package example    private[example] class PackagePrivate"
      val extractor = new TargetExtractor(config)
      val targets = extractor.extractFromDefOnly(defOnly, Nil)
      targets.size should equal(1)
      targets(0).typeName should equal("PackagePrivate")
    }
    {
      val defOnly = "package example    private[example] object PackagePrivate"
      val extractor = new TargetExtractor(config)
      val targets = extractor.extractFromDefOnly(defOnly, Nil)
      targets.size should equal(1)
      targets(0).typeName should equal("PackagePrivate")
    }
    {
      val defOnly = "package example    private[example] trait PackagePrivate"
      val extractor = new TargetExtractor(config)
      val targets = extractor.extractFromDefOnly(defOnly, Nil)
      targets.size should equal(1)
      targets(0).typeName should equal("PackagePrivate")
    }
  }

  test("extract package protected class/trait/object from defOnly") {
    {
      val defOnly = "package example    protected class Protected"
      val extractor = new TargetExtractor(config)
      val targets = extractor.extractFromDefOnly(defOnly, Nil)
      targets.size should equal(1)
      targets(0).typeName should equal("Protected")
    }
    {
      val defOnly = "package example    protected object Protected"
      val extractor = new TargetExtractor(config)
      val targets = extractor.extractFromDefOnly(defOnly, Nil)
      targets.size should equal(1)
      targets(0).typeName should equal("Protected")
    }
    {
      val defOnly = "package example    protected trait Protected"
      val extractor = new TargetExtractor(config)
      val targets = extractor.extractFromDefOnly(defOnly, Nil)
      targets.size should equal(1)
      targets(0).typeName should equal("Protected")
    }
  }

  test("extract case class (HttpResponse)") {
    val defOnly = "case class HttpResponse(val statusCode: Int," +
      "    val headers: Map[String, List[String]]," +
      "    val content: String)"
    val extractor = new TargetExtractor(config)
    val targets = extractor.extractFromDefOnly(defOnly, Nil)
    targets.size should equal(1)
    targets(0).typeName should equal("HttpResponse")
  }

  test("extract import def before the target def from defOnly") {
    val defOnly = "package example         object IO"
    val extractor = new TargetExtractor(config)
    val targets = extractor.extractFromDefOnly(defOnly, Nil)
    targets.size should equal(1)
    targets(0).typeName should equal("IO")
  }

  test("extract scoped class/object/trait from defOnly") {
    // no need to support private class/object/trait
    // because it should be package private, maybe..
    val defOnly = "package example  " +
      "private[example] class PackagePrivateScopeClass  " +
      "protected class ProtectedScopeClass  " +
      "private[example] object PackagePrivateScopeObject  " +
      "protected object ProtectedScopeObject  " +
      "private[example] trait PackagePrivateScopeTrait  " +
      "protected trait ProtectedScopeTrait  "
    val extractor = new TargetExtractor(config)
    val targets = extractor.extractFromDefOnly(defOnly, Nil)
    targets.size should equal(6)
  }

  test("extract class with type parameters from defOnly") {
    val defOnly = "package example.typeparameters " +
      "class WithTypeParameters[T] " +
      "class StructuralType[Foo <: ] " +
      "class CoVariant[+T] " +
      "class ContraVariant[-T] " +
      "class UpperBound[T <: Any] " +
      "class LowerBound[T >: TraversableOnce[String]]" +
      "trait WithTypeParametersTrait[T] " +
      "trait StructuralTypeTrait[Foo <: ] " +
      "trait CoVariantTrait[+T] " +
      "trait ContraVariantTrait[-T] " +
      "trait UpperBoundTrait[T <: Any] " +
      "trait LowerBoundTrait[T >: TraversableOnce[String]]"
    val extractor = new TargetExtractor(config)
    val targets = extractor.extractFromDefOnly(defOnly, Nil)
    targets.size should equal(12)
  }

  test("extract from the code which contains semicolon") {
    val lines = List(
      "package com.example",
      "import com.example.util._",
      "class Sample1;",
      "class Sample2(name: String = \"\") {",
      "  def doSomething() = println(\"foo\")",
      "}"
    )
    val (result, _) = extractor.getDefOnlyAndImportedList(lines)
    val expected = "package com.example\\s+" +
      "class Sample1\\s+" +
      "class Sample2\\(name: String = \"\"\\)\\s*"
    result.matches(expected) should equal(true)
  }

  test("extract from the source code") {
    {
      val lines = List("package com.example     ",
        "case class Format(val value: String = \"\") ",
        "case class Query(val value: String = \"grouped\") extends RequestParam"
      )
      val (defOnly, _) = extractor.getDefOnlyAndImportedList(lines)
      val expected = "package com.example      " +
        "case class Format(val value: String = \"\")  " +
        "case class Query(val value: String = \"\") extends RequestParam"
      defOnly should equal(expected)
    }
  }

}