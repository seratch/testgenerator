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

object Command {

  def main(args: Array[String]) {

    val env_srcDir = System.getProperty("testgen.srcDir")
    val env_srcTestDir = System.getProperty("testgen.srcTestDir")
    val srcDir = if (env_srcDir.isEmpty) "src/main/scala" else env_srcDir
    val srcTestDir = if (env_srcTestDir.isEmpty) "src/test/scala" else env_srcTestDir
    val config = new Config(
      srcDir = srcDir,
      srcTestDir = srcTestDir
    )

    val generator = new TestGenerator(config)

    val pathOrPackage = args(0)
    val targets = new TargetExtractor(config).extract(pathOrPackage)
    targets match {
      case Nil => {
        println("Cannot find the targets to generate test...")
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