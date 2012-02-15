package testgen

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class TargetParameterSuite extends FunSuite with ShouldMatchers {

  test("available") {
    val name: String = ""
    val typeName: String = ""
    val instance = new TargetParameter(name, typeName)
    instance should not be null
  }

}
