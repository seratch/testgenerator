import sbt._
import Keys._

object TestgenCoreBuild extends Build {

  lazy val testgenCore = Project("testgen-core", file("."), settings = mainSettings)

  lazy val mainSettings: Seq[Project.Setting[_]] = Defaults.defaultSettings ++ Seq(
    sbtPlugin := false,
    organization := "com.github.seratch",
    name := "testgen-core",
    version := "0.2-SNAPSHOT",
    publishTo := Some(
      Resolver.file(
        "Github Pages", 
        Path.userHome / "github" / "seratch.github.com" / "mvn-repo" / "releases" asFile
      )
      (Patterns(true, Resolver.mavenStyleBasePattern))
    ),
    publishMavenStyle := true,
    scalacOptions ++= Seq("-deprecation", "-unchecked")
  )

}

