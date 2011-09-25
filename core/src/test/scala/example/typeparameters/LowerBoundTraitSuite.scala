package example.typeparameters

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class LowerBoundTraitSuite extends FunSuite with ShouldMatchers {

  type ? = this.type

  test("available") {
    val mixined = new Object with LowerBoundTrait[Any]
    mixined should not be null
  }

}
