package example

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ReadableSObjectSuite extends FunSuite with ShouldMatchers {

  type ? = this.type

  test("available") {
    ReadableSObject.isInstanceOf[Singleton] should equal(true)
  }

}
