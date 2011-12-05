name := "testgen-core"

organization := "com.github.seratch"

crossScalaVersions := Seq("2.9.1", "2.9.0-1", "2.9.0")

scalaVersion := "2.9.1"

resolvers ++= Seq(
  "snapshots" at "http://scala-tools.org/repo-snapshots",
  "releases"  at "http://scala-tools.org/repo-releases"
)

libraryDependencies <++= (scalaVersion) { scalaVersion =>
  val specsArtifactId = scalaVersion match {
    case "2.9.1" => "specs_2.9.0"
    case _       => "specs_" + scalaVersion
  }
  Seq(
    "junit" % "junit" % "4.9" % "test",
    "org.scalatest" %% "scalatest" % "1.6.1" % "test",
    "org.scala-tools.testing" % specsArtifactId % "1.6.8" % "test",
    "org.specs2" %% "specs2" % "1.5" % "test",
    "org.scala-tools.testing" %% "scalacheck" % "1.9" % "test"
  )
}


