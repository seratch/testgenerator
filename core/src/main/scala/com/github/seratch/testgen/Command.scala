package com.github.seratch.testgen

import java.io.File
  
object Command {
  def generateTests(pathOrPackage: File, config: Config): Seq[Test] = {
    val testGenerator = new TestGenerator(config)
    val extractor = new TargetExtractor(config)
    extractor.extractAllFilesRecursively(pathOrPackage) map testGenerator.generate
  }
}
