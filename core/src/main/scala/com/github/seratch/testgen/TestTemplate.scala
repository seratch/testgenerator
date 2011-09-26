package com.github.seratch.testgen

/*
 * Copyright 2011 Kazuhiro SERA.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, 
 * either express or implied. See the License for the specific language 
 * governing permissions and limitations under the License. 
 */

case class TestTemplate(name: String)

object TestTemplate {

  val ScalaTestFunSuite = TestTemplate("scalatest.FunSuite")

  val ScalaTestAssertions = TestTemplate("scalatest.Assertions")

  val ScalaTestSpec = TestTemplate("scalatest.Spec")

  val ScalaTestWordSpec = TestTemplate("scalatest.WordSpec")

  val ScalaTestFlatSpec = TestTemplate("scalatest.FlatSpec")

  val ScalaTestFeatureSpec = TestTemplate("scalatest.FeatureSpec")

  val SpecsSpecification = TestTemplate("specs.Specification")

  val Specs2Specification = TestTemplate("specs2.Specification")

}