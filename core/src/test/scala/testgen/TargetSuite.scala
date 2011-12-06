package testgen

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class TargetSuite extends FunSuite with ShouldMatchers {

  type ? = this.type // for IntelliJ IDEA

  test("available") {
    val defType: DefType = null
    val importList: List[String] = Nil
    val fullPackageName: String = ""
    val typeName: String = ""
    val parameters: List[TargetParameter] = Nil
    val instance = new Target(defType, importList, fullPackageName, typeName, parameters)
    instance should not be null
  }

}
