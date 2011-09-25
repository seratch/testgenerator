package example

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class CaseClassSuite extends FunSuite with ShouldMatchers {

  type ? = this.type

  test("available") {
    val v: String = null
    val instance = new CaseClass(v)
    instance should not be null
  }

}
