package example

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class FinalObjectSuite extends FunSuite with ShouldMatchers {

  test("available") {
    FinalObject.isInstanceOf[Singleton] should equal(true)
  }

}
