package com.github.seratch.testgen

object Command {

  def main(args: Array[String]) {
    val path = args(0)
    val extractor = new TargetExtractor
    val testgen = new TestGenerator
    val targets = extractor.extract(path)
    targets foreach {
      case target => {
        testgen.generate(target).createFileIfNotExist()
      }
    }
  }

}