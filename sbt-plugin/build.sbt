sbtPlugin := true

name := "testgen-sbt"

organization := "com.github.seratch"

crossScalaVersions := Seq("2.9.1", "2.9.0-1", "2.9.0")

scalaVersion := "2.9.1"

resolvers ++= Seq(
  "seratch.github.com releases"  at "http://seratch.github.com/mvn-repo/releases",
  "seratch.github.com snapshots" at "http://seratch.github.com/mvn-repo/snapshots"
)

libraryDependencies ++= Seq(
  "com.github.seratch" %% "testgen-core" % "0.1"
)

seq(lsSettings :_*)
