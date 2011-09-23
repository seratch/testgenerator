package com.github.seratch.testgen

object Command {

  def main(args: Array[String]) {
    val path = args(0)
    val generator = new TestGenerator
    val targets = new TargetExtractor().extract(path)
    targets foreach {
      case target => {
        generator.generate(target).createFileIfNotExist()
      }
    }
  }

}