package example.typeparameters

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class UpperBoundTraitSuite extends FunSuite with ShouldMatchers {

  test("available") {
    val mixedin = new Object with UpperBoundTrait[List[String]]
    mixedin should not be null
  }

}
