package example

class Sample

import org.specs.Specification
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest._
import org.scalatest.matchers._
import org.junit.Test

@RunWith(classOf[JUnitRunner])
class SampleSpecsSpec extends Specification {

  "Sample" should {
    "be available" in {
      val instance = new Sample()
      instance must notBeNull
    }
  }

}

import org.specs2.mutable.{Specification => Specs2Specification}

@RunWith(classOf[JUnitRunner])
class SampleSpecs2Spec extends Specs2Specification {

  "Sample" should {
    "be available" in {
      val instance = new Sample()
      instance must not beNull
    }
  }

}

@RunWith(classOf[JUnitRunner])
class SampleFunSuite extends FunSuite with ShouldMatchers {

  type ? = this.type // for IntelliJ IDEA

  test("available") {
    val instance = new Sample()
    instance should not be null
  }

}

@RunWith(classOf[JUnitRunner])
class SampleFunSuiteWithMustMatchers extends FunSuite with MustMatchers {

  type ? = this.type

  test("available") {
    val instance = new Sample()
    instance must not be null
  }

}

@RunWith(classOf[JUnitRunner])
class SampleFunSuiteWithoutMatcher extends FunSuite {

  type ? = this.type

  test("available") {
    val instance = new Sample()
    assert(instance != null)
  }

}

class SampleAssertionsSuite extends Assertions with ShouldMatchers {

  type ? = this.type

  @Test def available {
    val instance = new Sample()
    instance should not be null
  }

}

@RunWith(classOf[JUnitRunner])
class SampleSpec extends Spec with ShouldMatchers {

  type ? = this.type

  describe("Sample") {
    it("should be available") {
      val instance = new Sample()
      instance should not be null
    }
  }

}

@RunWith(classOf[JUnitRunner])
class SampleWordSpec extends WordSpec with ShouldMatchers {

  type ? = this.type

  "Sample" should {
    "be available" in {
      val instance = new Sample()
      instance should not be null
    }
  }

}

@RunWith(classOf[JUnitRunner])
class SampleFlatSpec extends FlatSpec with ShouldMatchers {

  type ? = this.type

  "Sample" should "be available" in {
    val instance = new Sample()
    instance should not be null
  }

}

@RunWith(classOf[JUnitRunner])
class SampleFeatureSpec extends FeatureSpec with ShouldMatchers {

  type ? = this.type

  feature("Sample") {
    scenario("it is prepared") {
      val instance = new Sample()
      instance should not be null
    }
  }

}
