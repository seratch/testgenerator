package example

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import java.io._
import java.applet._

@RunWith(classOf[JUnitRunner])
class Semicolon1Suite extends FunSuite with ShouldMatchers {

  test("available") {
    val instance = new Semicolon1()
    instance should not be null
  }

}
