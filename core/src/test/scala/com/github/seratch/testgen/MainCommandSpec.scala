package com.github.seratch.testgen

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class MainCommandSpec extends Spec with MustMatchers {

  type ? = this.type // for IntelliJ IDEA

  describe("MainCommand") {
    it("should be available") {
      MainCommand.isInstanceOf[Singleton] must equal(true)
    }
  }

}
