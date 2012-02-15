package example

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class CaseObjectSuite extends FunSuite with ShouldMatchers {

  test("available") {
    CaseObject.isInstanceOf[Singleton] should equal(true)
  }

}
