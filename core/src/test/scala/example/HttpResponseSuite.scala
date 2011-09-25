package example

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class HttpResponseSuite extends FunSuite with ShouldMatchers {

  type ? = this.type

  test("available") {
    val statusCode: Int = 0
    val headers: Map[String, List[String]] = Map()
    val content: String = null
    val instance = new HttpResponse(statusCode, headers, content)
    instance should not be null
  }

}
