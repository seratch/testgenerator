package testgen

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class DefTypeSuite extends FunSuite with ShouldMatchers {

  test("available") {
    val defType: String = ""
    val instance = new DefType(defType)
    instance should not be null
  }

}
