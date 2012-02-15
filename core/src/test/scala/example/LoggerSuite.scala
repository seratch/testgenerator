package example

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class LoggerSuite extends FunSuite with ShouldMatchers {

  test("available") {
    val instance = new Logger()
    instance should not be null
  }

}
