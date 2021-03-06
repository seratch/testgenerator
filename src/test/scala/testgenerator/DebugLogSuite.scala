package testgenerator

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class DebugLogSuite extends FunSuite with ShouldMatchers {

  test("available") {
    val config = new Config
    val debugLog = new DebugLog(config)
    debugLog should not be null
    debugLog.ifDebug("debug log test")
  }

}
