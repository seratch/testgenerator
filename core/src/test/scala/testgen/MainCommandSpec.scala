package testgen

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class MainCommandSpec extends Spec with MustMatchers {

  describe("MainCommand") {
    it("should be available") {
      MainCommand.isInstanceOf[Singleton] must equal(true)
    }
  }

}
