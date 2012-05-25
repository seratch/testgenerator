package testgenerator

import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class TargetParserSuite extends FunSuite with ShouldMatchers {

  val importList = List("util._", "com.example.bean.SampleBean")

  test("extract classes defined no arg") {
    {
      val input = "class MyClass    class MyClass2   "
      val parser = new TargetParser("com.example", importList)
      val result = parser.parse(parser.classDef, input)
      result.get.size should equal(2)
      result.get(0).typeName should equal("MyClass")
      result.get(1).typeName should equal("MyClass2")
    }
  }

  test("extract classes defined with args") {
    {
      val input = "class MyClass(value: String) class MyClass2(value: String, age:Int) "
      val parser = new TargetParser("com.example", importList)
      val result = parser.parse(parser.classWithConstructorDef, input)
      result.get.size should equal(2)
      result.get(0).typeName should equal("MyClass")
      result.get(1).typeName should equal("MyClass2")
    }
    {
      val input = "class MyClass(l1: List[String], l2: List[String]) class MyClass2(value: String, age:Int) "
      val parser = new TargetParser("com.example", importList)
      val result = parser.parse(parser.classWithConstructorDef, input)
      result.get.size should equal(2)
      result.get(0).typeName should equal("MyClass")
      result.get(1).typeName should equal("MyClass2")
    }
    {
      val input = "class MyClass(l: List[String], m: Map[String,String], str: String) class MyClass2(value: String, age:Int) "
      val parser = new TargetParser("com.example", importList)
      val result = parser.parse(parser.classWithConstructorDef, input)
      result.get.size should equal(2)
      result.get(0).typeName should equal("MyClass")
      result.get(1).typeName should equal("MyClass2")
    }
    {
      val input = "class MyClass(value: String) class MyClass2(value: String, age:Int) "
      val parser = new TargetParser("com.example", importList)
      val result = parser.parse(parser.classWithConstructorDef, input)
      result.get.size should equal(2)
      result.get(0).typeName should equal("MyClass")
      result.get(1).typeName should equal("MyClass2")
    }
  }

  test("extract clasess defined args with default values") {
    {
      val input = "case class MyClass(val value: String)"
      val parser = new TargetParser("com.example", importList)
      val result = parser.parse(parser.allDef, input)
      result.get.size should equal(1)
      result.get(0).typeName should equal("MyClass")
      result.get(0).parameters(0).name should equal("value")
    }
    {
      val input = "case class MyClass(val className: String)"
      val parser = new TargetParser("com.example", importList)
      val result = parser.parse(parser.allDef, input)
      result.get.size should equal(1)
      result.get(0).typeName should equal("MyClass")
      result.get(0).parameters(0).name should equal("className")
    }
    {
      val input = "case class MyClass(val objectName: String)"
      val parser = new TargetParser("com.example", importList)
      val result = parser.parse(parser.allDef, input)
      result.get.size should equal(1)
      result.get(0).typeName should equal("MyClass")
      result.get(0).parameters(0).name should equal("objectName")
    }
    {
      val input = "case class MyClass(val traitName: String)"
      val parser = new TargetParser("com.example", importList)
      val result = parser.parse(parser.allDef, input)
      result.get.size should equal(1)
      result.get(0).typeName should equal("MyClass")
      result.get(0).parameters(0).name should equal("traitName")
    }
    {
      val input = "case class MyClass(val finalAnswer: String)"
      val parser = new TargetParser("com.example", importList)
      val result = parser.parse(parser.allDef, input)
      result.get.size should equal(1)
      result.get(0).typeName should equal("MyClass")
      result.get(0).parameters(0).name should equal("finalAnswer")
    }
    {
      val input = """case class Config(encoding: String = "UTF-8",
                  srcDir: String = "src/main/scala",
                  srcTestDir: String = "src/test/scala",
                  testTemplate: TestTemplate = TestTemplate.ScalaTestFunSuite,
                  scalaTestMatchers: ScalaTestMatchers = ScalaTestMatchers.Should)
      """
      val parser = new TargetParser("com.example", importList)
      val result = parser.parse(parser.allDef, input)
      result.get.size should equal(1)
      result.get(0).typeName should equal("Config")
      result.get(0).parameters(0).name should equal("encoding")
      result.get(0).parameters(1).name should equal("srcDir")
      result.get(0).parameters(2).name should equal("srcTestDir")
      result.get(0).parameters(3).name should equal("testTemplate")
      result.get(0).parameters(4).name should equal("scalaTestMatchers")
    }
  }

  test("extract classes annotated(the annotaion has no arg)") {
    {
      val input = "@scala.annotation.tailrec class MyClass object MyObject"
      val parser = new TargetParser("com.example", importList)
      val result = parser.parse(parser.allDef, input)
      result.get.size should equal(2)
      result.get(0).typeName should equal("MyClass")
      result.get(1).typeName should equal("MyObject")
    }
    {
      val input = "@Path() class MyController object MyObject"
      val parser = new TargetParser("com.example", importList)
      val result = parser.parse(parser.allDef, input)
      result.get.size should equal(2)
      result.get(0).typeName should equal("MyController")
      result.get(1).typeName should equal("MyObject")
    }
  }

  test("extract classes annotated(the annotation has value only)") {
    {
      val input = "@Path(foo) class MyController object MyObject"
      val parser = new TargetParser("com.example", importList)
      val result = parser.parse(parser.allDef, input)
      result.get.size should equal(2)
      result.get(0).typeName should equal("MyController")
      result.get(1).typeName should equal("MyObject")
    }
    {
      val input = "@Path(\"foo\") class MyController object MyObject"
      val parser = new TargetParser("com.example", importList)
      val result = parser.parse(parser.allDef, input)
      result.get.size should equal(2)
      result.get(0).typeName should equal("MyController")
      result.get(1).typeName should equal("MyObject")
    }
    {
      val input = "@Path(\"/foo\") class MyController object MyObject"
      val parser = new TargetParser("com.example", importList)
      val result = parser.parse(parser.allDef, input)
      result.get.size should equal(2)
      result.get(0).typeName should equal("MyController")
      result.get(1).typeName should equal("MyObject")
    }
    {
      val input = "@Path(\"\"\"ssss\"\"\") class MyController object MyObject"
      val parser = new TargetParser("com.example", importList)
      val result = parser.parse(parser.allDef, input)
      result.get.size should equal(2)
      result.get(0).typeName should equal("MyController")
      result.get(1).typeName should equal("MyObject")
    }
  }

  test("extract classes annotated(the annotation has value and value)") {
    {
      val input = "@Foo(value = \"/baz\", key = \"ddd\") class Foo object MyObject"
      val parser = new TargetParser("com.example", importList)
      val result = parser.parse(parser.allDef, input)
      result.get.size should equal(2)
      result.get(0).typeName should equal("Foo")
      result.get(1).typeName should equal("MyObject")
    }
    {
      val input = "@Foo(value = \"\\xxx\\\") class Foo object MyObject"
      val parser = new TargetParser("com.example", importList)
      val result = parser.parse(parser.allDef, input)
      result.get.size should equal(2)
      result.get(0).typeName should equal("Foo")
      result.get(1).typeName should equal("MyObject")
    }
    {
      val input = "@Foo(c = 'c') class Foo object MyObject"
      val parser = new TargetParser("com.example", importList)
      val result = parser.parse(parser.allDef, input)
      result.get.size should equal(2)
      result.get(0).typeName should equal("Foo")
      result.get(1).typeName should equal("MyObject")
    }
  }

  test("extract classes defined with args annotated)") {
    {
      val input = "class MyClass(@reflect.BeanProperty value: String) class MyClass2(@BeanProperty val value: String, age:Int) "
      val parser = new TargetParser("com.example", importList)
      val result = parser.parse(parser.allDef, input)
      result.get.size should equal(2)
      result.get(0).typeName should equal("MyClass")
      result.get(1).typeName should equal("MyClass2")
    }
  }

  test("extract classes which extend something") {
    {
      val input = """class MyClass(value: String = "foo") extends SomeTrait"""
      val parser = new TargetParser("com.example", importList)
      val result = parser.parse(parser.allDef, input)
      result.get.size should equal(1)
      result.get(0).typeName should equal("MyClass")
    }
    {
      val input = "class ExtendedSomething extends Something  class ReadableSomething extends Something with Readable"
      val parser = new TargetParser("com.example", importList)
      val result = parser.parse(parser.allDef, input)
      result.get.size should equal(2)
      result.get(0).typeName should equal("ExtendedSomething")
      result.get(1).typeName should equal("ReadableSomething")
    }
    {
      val input = """class MyClass(value: String = "foo") extends SomeTrait with AnotherTrait"""
      val parser = new TargetParser("com.example", importList)
      val result = parser.parse(parser.allDef, input)
      result.get.size should equal(1)
      result.get(0).typeName should equal("MyClass")
    }
  }

  test("extract objects which extends something") {
    {
      val input = "object ExtendedSomething extends Something  object ReadableSomething extends Something with Readable"
      val parser = new TargetParser("com.example", importList)
      val result = parser.parse(parser.allDef, input)
      result.get.size should equal(2)
      result.get(0).typeName should equal("ExtendedSomething")
      result.get(1).typeName should equal("ReadableSomething")
    }
  }

  test("extract traits which extends something") {
    {
      val input = "trait ExtendedSomething extends Something  trait ReadableSomething extends Something with Readable"
      val parser = new TargetParser("com.example", importList)
      val result = parser.parse(parser.allDef, input)
      result.get.size should equal(2)
      result.get(0).typeName should equal("ExtendedSomething")
      result.get(1).typeName should equal("ReadableSomething")
    }
  }

  test("extract classes defined args(with default value)") {
    {
      val input = """class MyClass(value: String = "foo") class MyClass2(value: Bean = new Bean()) """
      val parser = new TargetParser("com.example", importList)
      val result = parser.parse(parser.allDef, input)
      result.get.size should equal(2)
      result.get(0).typeName should equal("MyClass")
      result.get(1).typeName should equal("MyClass2")
    }
    {
      val input = """class MyClass(value: String = "foo") class MyClass2(value: String = new String("var"), age:Int = 123) """
      val parser = new TargetParser("com.example", importList)
      val result = parser.parse(parser.allDef, input)
      result.get.size should equal(2)
      result.get(0).typeName should equal("MyClass")
      result.get(1).typeName should equal("MyClass2")
    }
    {
      val input = "case class SolrAddRequest(" +
        "@BeanProperty var core: SolrCore = new SolrCore(),                       " +
        "var documents: List[String] = Nil,                       " +
        "@BeanProperty var writerType: WriterType = WriterType.Standard,                       " +
        "@BeanProperty var additionalQueryString: String = \"\")"
      val parser = new TargetParser("com.example", importList)
      val result = parser.parse(parser.allDef, input)
      result.get.size should equal(1)
      result.get(0).typeName should equal("SolrAddRequest")
    }
    {
      val input = """class MyClass(value: String = "foo") class MyClass2(value: F = new F(1,2), age:Int = 123) """
      val parser = new TargetParser("com.example", importList)
      val result = parser.parse(parser.allDef, input)
      result.get.size should equal(2)
      result.get(0).typeName should equal("MyClass")
      result.get(1).typeName should equal("MyClass2")
    }
  }

  test("extract classes which have empty string default values") {
    {
      val input = "case class GroupField(@BeanProperty val field: String = \"\") extends RequestParam           " +
        "case class GroupQuery(@BeanProperty val query: String = \"grouped\") extends RequestParam           "
      val parser = new TargetParser("com.example", importList)
      val result = parser.parse(parser.allDef, input)
      result.get.size should equal(2)
    }
  }

  test("extract classes which have floatingPointNumber default values") {
    {
      val input = """
      case class MaxAlternateFieldLength(@BeanProperty val maxAlternateFieldLength: Int = -1) extends RequestParam
      case class RegexFragmenterSlop(@BeanProperty val regexSlop: Double = 0.6) extends RequestParam
      """
      val parser = new TargetParser("com.example", importList)
      val result = parser.parse(parser.allDef, input)
      result.get.size should equal(2)
    }
  }

  test("extract classes which have classOf[Type]") {
    {
      val input = """class HttpSolrClient(
        val log: Log = new Log(LoggerFactory.getLogger(classOf[Type].getCanonicalName))
      ) extends Something"""
      val parser = new TargetParser("com.example", importList)
      val result = parser.parse(parser.allDef, input)
      result.get.size should equal(1)
    }
  }

  test("extract classes which have asInstanceOf[Type]") {
    {
      val input = """class HttpSolrClient(
        val log: Log = something.asInstanceOf[Log]
      ) extends Something"""
      val parser = new TargetParser("com.example", importList)
      val result = parser.parse(parser.allDef, input)
      result.get.size should equal(1)
    }
  }

  test("extract classes which have args which omitted round bracket from new literal") {
    {
      val input = """class Document(
        val value: String = new String()
      )
      """
      val parser = new TargetParser("com.example", importList)
      val result = parser.parse(parser.allDef, input)
      result.get.size should equal(1)
    }
    {
      val input = """class Document(
        val value: String = new String
      )
      """
      val parser = new TargetParser("com.example", importList)
      val result = parser.parse(parser.allDef, input)
      result.get.size should equal(1)
    }
  }

  test("extract all targets") {
    val input = "class MyClass class MyClass2 class MyClass3 trait MyTrait object MyObject "
    val result = new TargetParser("com.example", importList).parse(input)
    result.getOrElse(Nil).size should equal(5)
  }

}
