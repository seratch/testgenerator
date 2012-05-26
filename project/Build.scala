import sbt._
import Keys._

object TestgeneratorBuild extends Build {

  lazy val testgenerator = Project("testgenerator", file("."), settings = mainSettings)

  lazy val mainSettings: Seq[Project.Setting[_]] = Defaults.defaultSettings ++ Seq(
    sbtPlugin := true,
    organization := "com.github.seratch",
    name := "testgenerator",
    version := "1.1.0",
    publishTo <<= version { (v: String) =>
      val nexus = "https://oss.sonatype.org/"
        if (v.trim.endsWith("SNAPSHOT")) Some("snapshots" at nexus + "content/repositories/snapshots")
        else Some("releases"  at nexus + "service/local/staging/deploy/maven2")
    },
    publishMavenStyle := true,
    scalacOptions ++= Seq("-deprecation", "-unchecked")
  )

}

