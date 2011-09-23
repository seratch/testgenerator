package com.example

import org.scalatest._
import org.scalatest.matchers._
import com.example.bean.SampleBean

class SampleBeanHolderSuite extends FunSuite with ShouldMatchers {

  type ? = this.type

  test("available") {
    val bean: SampleBean = null
    val instance = new SampleBeanHolder(bean)
    instance should not be null
  }

}
