package example

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class LogSuite extends FunSuite with ShouldMatchers {

  test("available") {
    val logger: Logger = null
    val instance = new Log(logger)
    instance should not be null
  }

}
