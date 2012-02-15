package testgen

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class TestTemplateSuite extends FunSuite with ShouldMatchers {

  test("available") {
    val name: String = ""
    val instance = new TestTemplate(name)
    instance should not be null
  }

}
