import sbt._
import Keys._

object TestgenCoreBuild extends Build {

  lazy val testgenCore = Project("testgen-core", file("."), settings = mainSettings)

  lazy val mainSettings: Seq[Project.Setting[_]] = Defaults.defaultSettings ++ Seq(
    sbtPlugin := false,
    organization := "com.github.seratch",
    name := "testgen-core",
    version := "0.2",
    publishTo <<= (version) { version: String =>
      Some(
        Resolver.file("GitHub Pages", Path.userHome / "github" / "seratch.github.com" / "mvn-repo" / {
          if (version.trim.endsWith("SNAPSHOT")) "snapshots" else "releases" 
        })
      )
    },
    publishMavenStyle := true,
    scalacOptions ++= Seq("-deprecation", "-unchecked")
  )

}

