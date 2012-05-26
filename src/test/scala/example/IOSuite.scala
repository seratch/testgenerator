package example

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class IOSuite extends FunSuite with ShouldMatchers {

  test("available") {
    val singleton = IO
    singleton should not be null
  }

}
