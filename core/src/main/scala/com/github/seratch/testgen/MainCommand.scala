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
package com.github.seratch.testgen

/**
 * MainCommand interface
 *
 * System.property
 *  -Dtestgen.srcDir
 *  -Dtestgen.srcTestDir
 *  -Dtestgen.encoding
 *  -Dtestgen.testTemplate
 *  -Dtestgen.scalatest.Matchers
 *
 */
object MainCommand {

  def main(args: Array[String]) {

    val envArgs: Map[String, String] = (args flatMap {
      case null => None
      case arg if arg.trim.startsWith("-") => {
        val arr = arg.trim.split("=")
        Some((arr.head.replaceFirst("-", "").trim, arr.tail.head.trim))
      }
      case _ => None
    }).toMap

    val env_srcDir: String = envArgs.getOrElse("testgen.srcDir", System.getProperty("testgen.srcDir"))
    val env_srcTestDir: String = envArgs.getOrElse("testgen.srcTestDir", System.getProperty("testgen.srcTestDir"))
    val env_encoding: String = envArgs.getOrElse("testgen.encoding", System.getProperty("testgen.encoding"))
    val env_testTemplate: String = envArgs.getOrElse("testgen.testTemplate", System.getProperty("testgen.testTemplate"))
    val env_scalaTestMatcher: String = envArgs.getOrElse("testgen.scalatest.Matchers", System.getProperty("testgen.scalatest.Matchers"))
    val env_debug: String = envArgs.getOrElse("testgen.debug", System.getProperty("testgen.debug"))

    val defaultConfig = new Config
    val srcDir = if (env_srcDir == null) defaultConfig.srcDir else env_srcDir
    val srcTestDir = if (env_srcTestDir == null) defaultConfig.srcTestDir else env_srcTestDir
    val encoding = if (env_encoding == null) defaultConfig.encoding else env_encoding
    val testTemplate = if (env_testTemplate == null) defaultConfig.testTemplate.name else env_testTemplate
    val scalaTestMatcher = if (env_scalaTestMatcher == null) defaultConfig.scalaTestMatchers.name else env_scalaTestMatcher
    val debug = if (env_debug == null) defaultConfig.debug else env_debug.toBoolean

    val config = new Config(
      encoding = encoding,
      srcDir = srcDir,
      srcTestDir = srcTestDir,
      testTemplate = new TestTemplate(testTemplate),
      scalaTestMatchers = new ScalaTestMatchers(scalaTestMatcher),
      debug = debug
    )

    val pathOrPackage = (args flatMap {
      case null => None
      case arg if arg.trim.startsWith("-") => None
      case arg => Some(arg.trim)
    }).head

    val executor = new TestgenExecutor(config)
    val tests = executor.generateTests(pathOrPackage)

    tests match {
      case Nil => println(ErrorMessage.noTargetsToGenerateFor(pathOrPackage))
      case allTests => allTests foreach (_.createFileIfNotExist())
    }

  }

}
