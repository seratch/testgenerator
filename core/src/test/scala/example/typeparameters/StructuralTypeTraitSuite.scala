package example.typeparameters

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class StructuralTypeTraitSuite extends FunSuite with ShouldMatchers {

  type ? = this.type

  test("available") {
    val mixedin = new Object with StructuralTypeTrait[String]
    mixedin should not be null
  }

}
