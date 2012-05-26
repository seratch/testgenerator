package testgenerator

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class MainCommandSuite extends FunSuite with ShouldMatchers {

  test("available") {
    MainCommand.isInstanceOf[Singleton] should equal(true)
  }

  test("specify -Dtestgenerator.testTemplate") {
    System.setProperty("testgenerator.testTemplate", "scalatest.Spec")
    System.setProperty("testgenerator.scalatest.Matchers", "MustMatchers")
    MainCommand.main(Array("testgenerator.MainCommand.scala"))
  }

  test("specify -testgenerator.testTemplate=scalatest.Spec as parameter") {
    System.setProperty("testgenerator.testTemplate", "xxx")
    System.setProperty("testgenerator.scalatest.Matchers", "xxx")
    System.setProperty("testgenerator.debug", "false")
    MainCommand.main(Array("testgenerator.MainCommand.scala", "-testgenerator.testTemplate=scalatest.Spec", "-testgenerator.scalatest.Matchers=MustMatchers"))
  }

}
