package example

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ProtectedScopeObjectSuite extends FunSuite with ShouldMatchers {

  type ? = this.type

  test("available") {
    ProtectedScopeObject.isInstanceOf[Singleton] should equal(true)
  }

}
