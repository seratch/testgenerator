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

import sbt._
import Path.sep
import Keys._
import java.io.File
import util.Properties.propOrNone

object TestGenKeys {
  val testgen = InputKey[Unit]("testgen")
  val isMavenStyle = SettingKey[Boolean]("is-maven-style")
  val encoding = SettingKey[String]("encoding")
  val testTemplate = SettingKey[String]("test-template") // we should change String to TestTemplate?
  val scalatestMatchers = SettingKey[String]("scalatest-matchers")
  val debug = SettingKey[Boolean]("debug")
}

object TestGenPlugin extends Plugin {
  import TestGenKeys._
  def createFileIfNotExists(f: File, s: String): Unit = {
    if(!f.exists) { IO.write(f, s) }
    else { println(f + " already exists") }
  }

  def packToDir(s: String): String = s.replace(".", sep.toString)

  val generateTest = inputTask { (argTask: TaskKey[Seq[String]]) => 
    (argTask, sourceDirectory in Compile, sourceDirectory in sbt.Test,
     organization, isMavenStyle, encoding, testTemplate, scalatestMatchers, debug) map {
      (args, main, test, org, isMavenStyle, encoding, testTemplate, matchers, debug) => {
        
        val pathOrPackage = args.headOption getOrElse ""
        
        val base =
          if(isMavenStyle) "scala" +sep+ packToDir(org)
          else "scala"
        
        val config = Config(encoding, testTemplate, matchers, debug)

        val tests: Seq[Test] = Command.generateTests(main / base / pathOrPackage, config)

        tests foreach { case Test(packageName, className, sourceCode) =>
          val testFileDir = test / base / (className + ".scala")
          createFileIfNotExists(testFileDir, sourceCode)
        }
      }
    }
  }

  
  val testGenSettings = Seq(
    testgen <<= generateTest,
    isMavenStyle := true,
    encoding := "UTF-8", 
    testTemplate := "scalatest.FunSuite",
    scalatestMatchers := "ShouldMatchers",
    debug := false
  )
  // you can override this in build.sbt or project/Build.scala
}
