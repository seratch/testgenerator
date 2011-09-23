package com.github.seratch.testgen

case class Target(defType: DefType,
                  importList: List[String] = Nil,
                  fullPackageName: String = "",
                  typeName: String,
                  parameters: List[TargetParameter] = Nil)

