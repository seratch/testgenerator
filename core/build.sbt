name := "testgen-core"

version := "0.1-SNAPSHOT"

organization := "com.github.seratch"

scalaVersion := "2.8.1"

resolvers ++= Seq(
  "snapshots" at "http://scala-tools.org/repo-snapshots",
  "releases"  at "http://scala-tools.org/repo-releases"
)

libraryDependencies ++= Seq(
  "junit" % "junit" % "4.9" % "test",
  "org.scalatest" %% "scalatest" % "1.5.1" % "test",
  "org.scala-tools.testing" %% "specs" % "1.6.8" % "test",
  "org.specs2" %% "specs2" % "1.5" % "test",
  "org.scala-tools.testing" %% "scalacheck" % "1.8" % "test"
)


