package example.typeparameters

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ContraVariantTraitSuite extends FunSuite with ShouldMatchers {

  test("available") {
    val mixedin = new Object with ContraVariantTrait[String]
    mixedin should not be null
  }

}
