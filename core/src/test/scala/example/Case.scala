package example

case class CaseClass(v: String)

case class HttpResponse(val statusCode: Int,
                        val headers: Map[String, List[String]],
                        val content: String)

case object CaseObject

