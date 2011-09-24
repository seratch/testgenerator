name := "testgen-core"

version := "0.1-SNAPSHOT"

organization := "com.github.seratch"

scalaVersion := "2.8.1"

libraryDependencies ++= Seq(
  "junit" % "junit" % "4.9" % "test",
  "org.scalatest" %% "scalatest" % "1.5.1" % "test",
  "org.scala-tools.testing" %% "scalacheck" % "1.8" % "test"
)

//seq(com.github.seratch.testgen.TestgenPlugin.settings: _*)

