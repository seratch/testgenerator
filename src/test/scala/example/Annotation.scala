package example

@scala.annotation.tailrec
class Annotation

case class Bean(@reflect.BeanProperty name: String)