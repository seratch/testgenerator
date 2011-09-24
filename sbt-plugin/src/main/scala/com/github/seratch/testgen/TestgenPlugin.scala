package com.github.seratch.testgen

import sbt.{Command => SbtCommand}
import sbt._
import Keys._
import com.github.seratch.testgen.{Command => TestgenCommand}

object TestgenPlugin extends Plugin {

  lazy val testgen = SbtCommand.single("testgen") {
    case (state, pathOrPackage) => {
      TestgenCommand.main(Array(pathOrPackage))
      state
    }
  }

  override lazy val settings = Seq(Keys.commands ++= Seq(testgen))

}
