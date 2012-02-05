package example

import reflect.BeanProperty

case class CaseClass(v: String)

case class HttpResponse(val statusCode: Int,
  val headers: Map[String, List[String]],
  val content: String)

class SolrCore

case class WriterType(name: String)

object WriterType {
  val Standard = WriterType("ssss")
}

case class SolrAddRequest(@BeanProperty var core: SolrCore = new SolrCore(),
  var documents: List[String] = Nil,
  @BeanProperty var writerType: WriterType = WriterType.Standard,
  @BeanProperty var additionalQueryString: String = "")

case object CaseObject

