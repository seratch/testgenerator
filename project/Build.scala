import sbt._
import Keys._

object ScalaTestGen extends Build {
  lazy val core = Project(
    "core",
    file("core"),
    settings = baseSettings
  )

  lazy val baseSettings = Defaults.defaultSettings ++ Seq(
    sbtPlugin := true,
    name := "testgen-core",
    version := "0.1",
    organization := "com.github.seratch",
    crossScalaVersions := Seq("2.9.1", "2.9.0-1", "2.9.0", "2.8.1"),
    resolvers ++= Seq(ScalaToolsSnapshots),
    libraryDependencies <++= (scalaVersion) { scalaVersion =>
      val scalatestVersion = scalaVersion match {
        case "2.8.1" => "1.5.1"
        case _       => "1.6.1"
      }
      val specsArtifactId = scalaVersion match {
        case "2.9.1" => "specs_2.9.0"
        case _       => "specs_" + scalaVersion
      }
      val scalacheckVersion = scalaVersion match {
        case "2.8.1" => "1.8"
        case _       => "1.9"
      }
      Seq(
        "junit" % "junit" % "4.9" % "test",
        "org.scalatest" %% "scalatest" % scalatestVersion % "test",
        "org.scala-tools.testing" % specsArtifactId % "1.6.8" % "test",
        "org.specs2" %% "specs2" % "1.5" % "test",
        "org.scala-tools.testing" %% "scalacheck" % scalacheckVersion % "test"
      )
    },
    publishTo := Some(
      Resolver.file(
        "Github Pages", 
        Path.userHome / "github" / "seratch.github.com" / "mvn-repo" / "releases" asFile
      )
      (Patterns(true, Resolver.mavenStyleBasePattern))
    ),
    publishMavenStyle := true,
    scalacOptions ++= Seq("-deprecation", "-unchecked")
  ) ++ ScriptedPlugin.scriptedSettings
}
