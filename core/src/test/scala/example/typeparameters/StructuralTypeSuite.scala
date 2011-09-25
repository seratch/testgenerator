package example.typeparameters

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class StructuralTypeSuite extends FunSuite with ShouldMatchers {

  type ? = this.type

  test("available") {
    val instance = new StructuralType[String]()
    instance should not be null
  }

}
