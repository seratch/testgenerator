package example

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class SomethingSuite extends FunSuite with ShouldMatchers {

  test("available") {
    val instance = new Something()
    instance should not be null
  }

}
