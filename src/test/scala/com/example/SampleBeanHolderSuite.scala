package com.example

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import com.example.bean.SampleBean

@RunWith(classOf[JUnitRunner])
class SampleBeanHolderSuite extends FunSuite with ShouldMatchers {

  test("available") {
    val bean: SampleBean = null
    val instance = new SampleBeanHolder(bean)
    instance should not be null
  }

}
