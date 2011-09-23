package com.github.seratch.testgen

case class Target(defType: DefType,
                  fullPackageName: String = "",
                  typeName: String,
                  parameters: List[TargetParameter] = Nil)

