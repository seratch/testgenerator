package example.typeparameters

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ContraVariantSuite extends FunSuite with ShouldMatchers {

  type ? = this.type

  test("available") {
    val instance = new ContraVariant[String]()
    instance should not be null
  }

}
