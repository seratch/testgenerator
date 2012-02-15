package testgen

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ConfigDefaultSuite extends FunSuite with ShouldMatchers {

  test("available") {
    val singleton = ConfigDefault
    singleton should not be null
  }

}
