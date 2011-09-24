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
      println(result)
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
      println(result)
      result.get.size should equal(2)
      result.get(0).typeName should equal("MyClass")
      result.get(1).typeName should equal("MyClass2")
    }
    {
      val input = "class MyClass(l1: List[String], l2: List[String]) class MyClass2(name: String, age:Int) "
      val parser = new TargetParser("com.example", importList)
      val result = parser.parse(parser.classWithConstructorDef, input)
      println(result)
      result.get.size should equal(2)
      result.get(0).typeName should equal("MyClass")
      result.get(1).typeName should equal("MyClass2")
    }
    {
      val input = "class MyClass(l: List[String], m: Map[String,String], str: String) class MyClass2(name: String, age:Int) "
      val parser = new TargetParser("com.example", importList)
      val result = parser.parse(parser.classWithConstructorDef, input)
      println(result)
      result.get.size should equal(2)
      result.get(0).typeName should equal("MyClass")
      result.get(1).typeName should equal("MyClass2")
    }
  }

  test("extract class defined args(with default typeName)") {
    {
      val input = """class MyClass(name: String = "foo") class MyClass2(name: String = new String("var"), age:Int = 123) """
      val parser = new TargetParser("com.example", importList)
      val result = parser.parse(parser.classWithConstructorDef, input)
      println(result)
      result.get.size should equal(2)
      result.get(0).typeName should equal("MyClass")
      result.get(1).typeName should equal("MyClass2")
    }
    {
      val input = """class MyClass(name: String = "foo") class MyClass2(name: F = new F(1,2), age:Int = 123) """
      val parser = new TargetParser("com.example", importList)
      val result = parser.parse(parser.classWithConstructorDef, input)
      println(result)
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