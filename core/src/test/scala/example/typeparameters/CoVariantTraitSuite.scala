package example.typeparameters

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class CoVariantTraitSuite extends FunSuite with ShouldMatchers {

  test("available") {
    val mixedin = new Object with CoVariantTrait[String]
    mixedin should not be null
  }

}
