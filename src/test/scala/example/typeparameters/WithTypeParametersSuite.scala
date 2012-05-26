package example.typeparameters

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
@RunWith(classOf[JUnitRunner])
class WithTypeParametersSuite extends FunSuite with ShouldMatchers {

  test("available") {
    val instance = new WithTypeParameters[List[String]]()
    instance should not be null
  }

}
