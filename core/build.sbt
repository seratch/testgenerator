crossScalaVersions := Seq("2.9.1", "2.9.0-1", "2.9.0")

scalaVersion := "2.9.1"

libraryDependencies <++= (scalaVersion) { scalaVersion =>
  val specsArtifactId = scalaVersion match {
    case "2.9.1" => "specs_2.9.0"
    case _       => "specs_" + scalaVersion
  }
  Seq(
    "junit" % "junit" % "4.10" % "test",
    "org.scalatest" %% "scalatest" % "1.6.1" % "test",
    "org.scala-tools.testing" % specsArtifactId % "1.6.8" % "test",
    "org.specs2" %% "specs2" % "1.5" % "test",
    "org.scala-tools.testing" %% "scalacheck" % "1.9" % "test"
  )
}

externalResolvers ~= (_.filter(_.name != "Scala-Tools Maven2 Repository"))

seq(testgenSettings: _*)

seq(scalariformSettings: _*)

// publish

publishMavenStyle := true

publishArtifact in Test := false

pomIncludeRepository := { x => false }

pomExtra := (
  <url>https://github.com/seratch/scala-testgen</url>
  <licenses>
    <license>
      <name>Apache License, Version 2.0</name>
      <url>http://www.apache.org/licenses/LICENSE-2.0.html</url>
      <distribution>repo</distribution>
    </license>
  </licenses>
  <scm>
    <url>git@github.com:seratch/scala-testgen.git</url>
    <connection>scm:git:git@github.com:seratch/scala-testgen.git</connection>
  </scm>
  <developers>
    <developer>
      <id>seratch</id>
      <name>Kazuhuiro Sera</name>
      <url>http://seratch.net/</url>
    </developer>
  </developers>
)

