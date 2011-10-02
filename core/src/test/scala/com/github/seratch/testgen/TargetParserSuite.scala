package com.github.seratch.testgen

import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class TargetParserSuite extends FunSuite with ShouldMatchers {

  type ? = this.type

  val importList = List("util._", "com.example.bean.SampleBean")

  test("extract class defined no arg") {
    {
      val input = "class MyClass    class MyClass2   "
      val parser = new TargetParser("com.example", importList)
      val result = parser.parse(parser.classDef, input)
      result.get.size should equal(2)
      result.get(0).typeName should equal("MyClass")
      result.get(1).typeName should equal("MyClass2")
    }
  }

  test("extract class(with args)") {
    {
      val input = "class MyClass(name: String) class MyClass2(name: String, age:Int) "
      val parser = new TargetParser("com.example", importList)
      val result = parser.parse(parser.classWithConstructorDef, input)
      result.get.size should equal(2)
      result.get(0).typeName should equal("MyClass")
      result.get(1).typeName should equal("MyClass2")
    }
    {
      val input = "class MyClass(l1: List[String], l2: List[String]) class MyClass2(name: String, age:Int) "
      val parser = new TargetParser("com.example", importList)
      val result = parser.parse(parser.classWithConstructorDef, input)
      result.get.size should equal(2)
      result.get(0).typeName should equal("MyClass")
      result.get(1).typeName should equal("MyClass2")
    }
    {
      val input = "class MyClass(l: List[String], m: Map[String,String], str: String) class MyClass2(name: String, age:Int) "
      val parser = new TargetParser("com.example", importList)
      val result = parser.parse(parser.classWithConstructorDef, input)
      result.get.size should equal(2)
      result.get(0).typeName should equal("MyClass")
      result.get(1).typeName should equal("MyClass2")
    }
    {
      val input = "class MyClass(name: String) class MyClass2(name: String, age:Int) "
      val parser = new TargetParser("com.example", importList)
      val result = parser.parse(parser.classWithConstructorDef, input)
      result.get.size should equal(2)
      result.get(0).typeName should equal("MyClass")
      result.get(1).typeName should equal("MyClass2")
    }
  }

  test("extract class with value") {
    {
      val input = "case class MyClass(val value: String)"
      val parser = new TargetParser("com.example", importList)
      val result = parser.parse(parser.allDef, input)
      result.get.size should equal(1)
      result.get(0).typeName should equal("MyClass")
      result.get(0).parameters(0).name should equal("value")
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

  test("extract class annotated - no arg") {
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

  test("extract class annotated - value only") {
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

  test("extract class annotated - name and value") {
    {
      val input = "@Foo(name = \"/baz\", key = \"ddd\") class Foo object MyObject"
      val parser = new TargetParser("com.example", importList)
      val result = parser.parse(parser.allDef, input)
      result.get.size should equal(2)
      result.get(0).typeName should equal("Foo")
      result.get(1).typeName should equal("MyObject")
    }
    {
      val input = "@Foo(name = \"\\xxx\\\") class Foo object MyObject"
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


  test("extract class (with args annotated)") {
    {
      val input = "class MyClass(@reflect.BeanProperty name: String) class MyClass2(@BeanProperty val name: String, age:Int) "
      val parser = new TargetParser("com.example", importList)
      val result = parser.parse(parser.allDef, input)
      result.get.size should equal(2)
      result.get(0).typeName should equal("MyClass")
      result.get(1).typeName should equal("MyClass2")
    }
  }

  test("extract class which extends something") {
    {
      val input = """class MyClass(name: String = "foo") extends SomeTrait"""
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
      val input = """class MyClass(name: String = "foo") extends SomeTrait with AnotherTrait"""
      val parser = new TargetParser("com.example", importList)
      val result = parser.parse(parser.allDef, input)
      result.get.size should equal(1)
      result.get(0).typeName should equal("MyClass")
    }
  }

  test("extract object which extends something") {
    {
      val input = "object ExtendedSomething extends Something  object ReadableSomething extends Something with Readable"
      val parser = new TargetParser("com.example", importList)
      val result = parser.parse(parser.allDef, input)
      result.get.size should equal(2)
      result.get(0).typeName should equal("ExtendedSomething")
      result.get(1).typeName should equal("ReadableSomething")
    }
  }

  test("extract trait which extends something") {
    {
      val input = "trait ExtendedSomething extends Something  trait ReadableSomething extends Something with Readable"
      val parser = new TargetParser("com.example", importList)
      val result = parser.parse(parser.allDef, input)
      result.get.size should equal(2)
      result.get(0).typeName should equal("ExtendedSomething")
      result.get(1).typeName should equal("ReadableSomething")
    }
  }

  test("extract class defined args(with default value)") {
    {
      val input = """class MyClass(name: String = "foo") class MyClass2(name: Bean = new Bean()) """
      val parser = new TargetParser("com.example", importList)
      val result = parser.parse(parser.allDef, input)
      result.get.size should equal(2)
      result.get(0).typeName should equal("MyClass")
      result.get(1).typeName should equal("MyClass2")
    }
    {
      val input = """class MyClass(name: String = "foo") class MyClass2(name: String = new String("var"), age:Int = 123) """
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
      val input = """class MyClass(name: String = "foo") class MyClass2(name: F = new F(1,2), age:Int = 123) """
      val parser = new TargetParser("com.example", importList)
      val result = parser.parse(parser.allDef, input)
      result.get.size should equal(2)
      result.get(0).typeName should equal("MyClass")
      result.get(1).typeName should equal("MyClass2")
    }
  }

  test("extract all targets") {
    val input = "class MyClass class MyClass2 class MyClass3 trait MyTrait object MyObject "
    val result = new TargetParser("com.example", importList).parse(input)
    result.getOrElse(Nil).size should equal(5)
  }

}
