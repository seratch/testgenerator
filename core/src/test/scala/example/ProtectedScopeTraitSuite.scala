package example

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ProtectedScopeTraitSuite extends FunSuite with ShouldMatchers {

  test("available") {
    val mixedin = new Object with ProtectedScopeTrait
    mixedin should not be null
  }

}
