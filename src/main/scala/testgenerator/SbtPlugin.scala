package testgenerator

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

import sbt._
import sbt.Keys._
import sbt.{ Test => SbtTest }
import testgenerator._

object SbtKeys {
  lazy val testgenerator = InputKey[Unit]("test-gen")
  lazy val testgeneratorEncoding = SettingKey[String]("test-gen-encoding")
  lazy val testgeneratorTestTemplate = SettingKey[String]("test-gen-test-template")
  lazy val testgeneratorScalaTestMatchers = SettingKey[String]("test-gen-scala-test-matchers")
  lazy val testgeneratorWithJUnitRunner = SettingKey[Boolean]("test-gen-with-junit-runner")
  lazy val testgeneratorLineBreak = SettingKey[String]("test-gen-line-break")
  lazy val testgeneratorDebug = SettingKey[Boolean]("test-gen-debug")
}

import SbtKeys._

object SbtPlugin extends Plugin {

  val testgeneratorTask = inputTask {
    (taskKey: TaskKey[Seq[String]]) =>
      {
        (taskKey, scalaSource in Compile, scalaSource in SbtTest,
          testgeneratorEncoding, testgeneratorTestTemplate, testgeneratorScalaTestMatchers, testgeneratorWithJUnitRunner,
          testgeneratorLineBreak, testgeneratorDebug) map {
            case (args, srcDir, srcTestDir, encoding, testTemplate, scalaTestMatchers, withJUnitRunner, lineBreak, debug) => {
              val testgeneratorExecutor = new TestgenExecutor(new Config(
                encoding = encoding,
                srcDir = srcDir.getAbsolutePath,
                srcTestDir = srcTestDir.getAbsolutePath,
                testTemplate = new TestTemplate(testTemplate),
                scalaTestMatchers = new ScalaTestMatchers(scalaTestMatchers),
                withJUnitRunner = withJUnitRunner,
                lineBreak = new LineBreak(lineBreak),
                debug = debug
              ))
              args match {
                case Nil => println("Usage: test-gen [CLASS/PACAKGE OR PATH]")
                case _ => {
                  val pathOrPackage = args.head
                  val tests = testgeneratorExecutor.generateTests(pathOrPackage)
                  tests match {
                    case Nil => println(ErrorMessage.noTargetsToGenerateFor(pathOrPackage))
                    case allTests => allTests foreach (test => test.createFileIfNotExist())
                  }
                }
              }
            }
          }
      }
  }

  val testgeneratorSettings = inConfig(Compile)(Seq(
    testgenerator <<= testgeneratorTask,
    testgeneratorEncoding := "UTF-8",
    testgeneratorTestTemplate := "scalatest.FlatSpec",
    testgeneratorScalaTestMatchers := "ShouldMatchers",
    testgeneratorWithJUnitRunner := false,
    testgeneratorLineBreak := "LF",
    testgeneratorDebug := false
  ))

}
