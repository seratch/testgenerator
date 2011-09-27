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
 * Command interface
 *
 * System.property
 *  -Dtestgen.srcDir
 *  -Dtestgen.srcTestDir
 *  -Dtestgen.encoding
 *  -Dtestgen.testTemplate
 *  -Dtestgen.scalatest.Matchers
 *
 */
object Command {

  def main(args: Array[String]) {

    val env_srcDir = System.getProperty("testgen.srcDir")
    val env_srcTestDir = System.getProperty("testgen.srcTestDir")
    val env_encoding = System.getProperty("testgen.encoding")
    val env_testTemplate = System.getProperty("testgen.testTemplate")
    val env_scalaTestMatcher = System.getProperty("testgen.scalatest.Matchers")

    val defaultConfig = new Config
    val srcDir = if (env_srcDir == null) defaultConfig.srcDir else env_srcDir
    val srcTestDir = if (env_srcTestDir == null) defaultConfig.srcTestDir else env_srcTestDir
    val encoding = if (env_encoding == null) defaultConfig.encoding else env_encoding
    val testTemplate = if (env_testTemplate == null) defaultConfig.testTemplate.name else env_testTemplate
    val scalaTestMatcher = if (env_scalaTestMatcher == null) defaultConfig.scalaTestMatchers.name else env_scalaTestMatcher

    val config = new Config(
      encoding = encoding,
      srcDir = srcDir,
      srcTestDir = srcTestDir,
      testTemplate = new TestTemplate(testTemplate),
      scalaTestMatchers = new ScalaTestMatchers(scalaTestMatcher)
    )

    val pathOrPackage = args(0)
    val targets = new TargetExtractor(config).extract(pathOrPackage)
    val generator = new TestGenerator(config)
    targets match {
      case Nil => {
        println("Cannot find the targets to generate test for \"" + pathOrPackage + "\"")
      }
      case all => {
        all foreach {
          case target => {
            generator.generate(target).createFileIfNotExist()
          }
        }
      }
    }

  }

}