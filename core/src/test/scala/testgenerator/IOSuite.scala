package testgenerator

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class IOSuite extends FunSuite with ShouldMatchers {

  test("available") {
    IO.isInstanceOf[Singleton] should equal(true)
  }

}
