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

object Config {
  def apply(encoding: Option[String], testTemplate: Option[String], scalaTestMatcher: Option[String], debug: Option[String]): Config = {
    val e = encoding getOrElse "UTF-8"
    val tt = testTemplate getOrElse "scalatest.FunSuite"
    val stm = scalaTestMatcher getOrElse "ShouldMatchers"
    val d = debug getOrElse "false"
    new Config(e, new TestTemplate(tt), new ScalaTestMatchers(stm), d.toBoolean)
  }
}
  
case class Config(encoding: String = "UTF-8",
                  testTemplate: TestTemplate = TestTemplate.ScalaTestFunSuite,
                  scalaTestMatchers: ScalaTestMatchers = ScalaTestMatchers.Should,
                  debug: Boolean = false)
