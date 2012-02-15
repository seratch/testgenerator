package testgen

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class LineBreakSuite extends FunSuite with ShouldMatchers {

  test("available") {
    val lineBreak: String = "LF"
    val lb = new LineBreak(lineBreak)
    lb should equal(LineBreak.LF)
  }

}
