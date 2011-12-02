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

import sbt.{ Command => SbtCommand, _ }
import Path.sep
import Keys._
import java.io.File
import util.Properties.propOrNone

object TestGenKeys {
  val genTest = InputKey[Unit]("gen-test")
}

object SbtIO {
  def createFileIfNotExists(f: File, s: String): Unit = {
    if(!f.exists) { IO.write(f, s) }
    else { println(f + " already exists") }
  }

  def packToDir(s: String): String = s.replace(".", sep.toString)
}

object TestGenPlugin extends Plugin {
  import TestGenKeys._
  import SbtIO._

  val generateTest = inputTask { (argTask: TaskKey[Seq[String]]) => 
    (argTask, baseDirectory, sourceDirectory in Compile , sourceDirectory in sbt.Test, organization) map {
      (args, base, main, test, org) => {
        val orgDir = packToDir(org)
        val pathOrPackage: File = main / "scala" / orgDir / (args.headOption getOrElse "")
        
        val config = Config(propOrNone("encoding"), propOrNone("testTemplate"), propOrNone("scalatest.Matchers"), propOrNone("debug"))

        val testGenerator = new TestGenerator(config)
        val extractor = new TargetExtractor(config)
  
        val tests: Seq[Test] =  extractor.extractAllFilesRecursively(pathOrPackage) map testGenerator.generate distinct

        tests foreach { case Test(packageName, className, sourceCode) =>
          val testFileDir = test / "scala" / (packToDir(packageName) + sep + (className + ".scala"))
          createFileIfNotExists(testFileDir, sourceCode)
        }
      }
    }
  }

  val testGenSettings = Seq(
    genTest <<= generateTest
  )
}
