/*
 * Copyright 2011 Kazuhiro Sera.
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
package testgen

object ConfigDefault {

  val encoding = "UTF-8"
  val srcDir = "src/main/scala"
  val srcTestDir = "src/test/scala"
  val testTemplate = TestTemplate.ScalaTestFunSuite
  val scalaTestMatchers = ScalaTestMatchers.Should
  val lineBreak = LineBreak.CRLF
  val debug = false

}

case class Config(encoding: String = ConfigDefault.encoding,
  srcDir: String = ConfigDefault.srcDir,
  srcTestDir: String = ConfigDefault.srcTestDir,
  testTemplate: TestTemplate = ConfigDefault.testTemplate,
  scalaTestMatchers: ScalaTestMatchers = ConfigDefault.scalaTestMatchers,
  lineBreak: LineBreak = LineBreak.CRLF,
  debug: Boolean = ConfigDefault.debug)

