package testgen

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ConfigSuite extends FunSuite with ShouldMatchers {

  test("available") {
    val encoding: String = ""
    val srcDir: String = ""
    val srcTestDir: String = ""
    val testTemplate: TestTemplate = null
    val scalaTestMatchers: ScalaTestMatchers = null
    val instance = new Config(encoding, srcDir, srcTestDir, testTemplate, scalaTestMatchers)
    instance should not be null
  }

}
