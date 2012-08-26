import sbt._
import Keys._

object TestgeneratorBuild extends Build {

  lazy val testgenerator = Project("testgenerator", file("."), settings = mainSettings)

  lazy val mainSettings: Seq[Project.Setting[_]] = Defaults.defaultSettings ++ Seq(
    sbtPlugin := true,
    organization := "com.github.seratch",
    name := "testgenerator",
    version := "1.1.0",
    scalaVersion := "2.9.2",
    libraryDependencies <++= (scalaVersion) { scalaVersion =>
      Seq(
        "junit" % "junit" % "4.10" % "test",
        "org.scalatest" %% "scalatest" % "1.7.2" % "test",
        "org.scala-tools.testing" % "specs_2.9.0" % "1.6.8" % "test",
        "org.specs2" %% "specs2" % "1.9" % "test"
     )
    },
    externalResolvers ~= (_.filter(_.name != "Scala-Tools Maven2 Repository")),
    publishMavenStyle := true,
    publishArtifact in Test := false,
    pomIncludeRepository := { x => false },
    pomExtra := (
      <url>https://github.com/seratch/testgenerator</url>
      <licenses>
        <license>
          <name>Apache License, Version 2.0</name>
          <url>http://www.apache.org/licenses/LICENSE-2.0.html</url>
          <distribution>repo</distribution>
        </license>
      </licenses>
      <scm>
        <url>git@github.com:seratch/testgenerator.git</url>
        <connection>scm:git:git@github.com:seratch/testgenerator.git</connection>
      </scm>
      <developers>
        <developer>
          <id>seratch</id>
          <name>Kazuhuiro Sera</name>
          <url>http://seratch.net/</url>
        </developer>
      </developers>
    ),
    publishTo <<= version { (v: String) =>
      val nexus = "https://oss.sonatype.org/"
        if (v.trim.endsWith("SNAPSHOT")) Some("snapshots" at nexus + "content/repositories/snapshots")
        else Some("releases"  at nexus + "service/local/staging/deploy/maven2")
    },
    scalacOptions ++= Seq("-deprecation", "-unchecked")
  )

}

