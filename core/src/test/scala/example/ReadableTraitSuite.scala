package example

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ReadableTraitSuite extends FunSuite with ShouldMatchers {

  type ? = this.type

  test("available") {
    val mixedin = new Object with ReadableTrait
    mixedin should not be null
  }

}
