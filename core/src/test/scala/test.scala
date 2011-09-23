import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class HelloWorldTest extends FunSuite {

  test("helloworld") {
    HelloWorld.say()
  }

}