package example

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class SolrCoreSuite extends FunSuite with ShouldMatchers {

  type ? = this.type

  test("available") {
    val instance = new SolrCore()
    instance should not be null
  }

}
