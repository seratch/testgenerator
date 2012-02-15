package testgen

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ScalaTestMatchersSuite extends FunSuite with ShouldMatchers {

  test("available") {
    val name: String = ""
    val instance = new ScalaTestMatchers(name)
    instance should not be null
  }

}
