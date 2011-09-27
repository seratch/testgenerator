package example

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import java.io._
import java.applet._

@RunWith(classOf[JUnitRunner])
class Semicolon2Suite extends FunSuite with ShouldMatchers {

  type ? = this.type // for IntelliJ IDEA

  test("available") {
    val v: String = ""
    val instance = new Semicolon2(v)
    instance should not be null
  }

}
