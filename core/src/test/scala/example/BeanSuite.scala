package example

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class BeanSuite extends FunSuite with ShouldMatchers {

  type ? = this.type

  test("available") {
    val name: String = ""
    val instance = new Bean(name)
    instance should not be null
  }

}
