package com.github.seratch.testgen

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class CommandSpec extends Spec with MustMatchers {

  type ? = this.type // for IntelliJ IDEA

  describe("Command") {
    it("should be available") {
      Command.isInstanceOf[Singleton] must equal(true)
    }
  }

}
