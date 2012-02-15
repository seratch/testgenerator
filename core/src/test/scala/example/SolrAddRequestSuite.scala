package example

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class SolrAddRequestSuite extends FunSuite with ShouldMatchers {

  test("available") {
    val core: SolrCore = null
    val documents: List[String] = Nil
    val writerType: WriterType = null
    val additionalQueryString: String = ""
    val instance = new SolrAddRequest(core, documents, writerType, additionalQueryString)
    instance should not be null
  }

}
