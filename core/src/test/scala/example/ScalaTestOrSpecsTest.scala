package example

import org.scalatest._
import org.scalatest.matchers._
import org.junit.Test

class ScalaTestOrSpecsTest extends Assertions {

  type ? = this.type

  @Test def available {
    val instance = new ScalaTestOrSpecs()
    assert(instance != null)
  }

}
