package example.typeparameters

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class LowerBoundSuite extends FunSuite with ShouldMatchers {

  test("available") {
    val instance = new LowerBound[Any]()
    instance should not be null
  }

}
