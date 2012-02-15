package example

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ScalaTestOrSpecsSpec extends Spec with ShouldMatchers {

  describe("ScalaTestOrSpecs") {
    it("should be available") {
      val instance = new ScalaTestOrSpecs()
      instance should not be null
    }
  }

}
