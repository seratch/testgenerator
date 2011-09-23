name := "scala-testgen-core"

version := "0.1-SNAPSHOT"

organization := "Kazuhiro Sera"

scalaVersion := "2.9.1"

libraryDependencies ++= Seq(
  "junit" % "junit" % "4.9" withSources(),
  "org.scalatest" %% "scalatest" % "1.6.1" withSources(),
//  "org.scala-tools.testing" %% "specs" % "1.6.8" withSources(),
//  "org.specs2" %% "specs2" % "1.5" withSources(),
  "org.scala-tools.testing" %% "scalacheck" % "1.9"
)
