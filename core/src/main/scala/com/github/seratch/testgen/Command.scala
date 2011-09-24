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
    val path = args(0)
    val generator = new TestGenerator
    val targets = new TargetExtractor().extract(path)
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