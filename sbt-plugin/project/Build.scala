import sbt._
import Keys._

object TestgenSbtBuild extends Build {

  lazy val testgenSbt = Project("testgen-sbt", file("."), settings = mainSettings)

  lazy val mainSettings: Seq[Project.Setting[_]] = Defaults.defaultSettings ++ Seq(
    sbtPlugin := true,
    organization := "com.github.seratch",
    name := "testgen-sbt",
    version := "0.1",
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

