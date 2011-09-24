// *** Put this under project/plugins/ ***

scalaVersion := "2.8.1"

resolvers ++= Seq(
  "seratch.github.com releases"  at "http://seratch.github.com/mvn-repo/releases",
  "seratch.github.com snapshots" at "http://seratch.github.com/mvn-repo/snapshots"
)

libraryDependencies ++= Seq(
  "com.github.seratch" %% "testgen-core" % "0.1-SNAPSHOT",
  "com.github.seratch" %% "testgen-sbt" % "0.1-SNAPSHOT"
)

