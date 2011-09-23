package com.github.seratch.testgen

import io.Source
import java.io.File

class TargetExtractor {

  // TODO several package

  def extract(path: String): List[Target] = {
    val lines = readLines(path)
    val defOnly = extractDefOnly(lines)
    defOnly.split("package").toList flatMap {
      case eachDefOnly => {
        val fullPackageName = eachDefOnly.trim.split("\\s+").toList.head
        val parser = new TargetParser(fullPackageName)
        val parserResult = parser.parse(eachDefOnly.replaceFirst(fullPackageName, ""))
        parserResult.getOrElse(Nil)
      }
    }
  }

  def readLines(path: String): List[String] = Source.fromFile(new File(path), "UTF-8").getLines.toList

  def extractDefOnly(lines: List[String]): String = {
    var isComment = false
    var blockDepth = 0
    (lines map {
      line => {
        var line_ = line
        if (line_.matches("\\s*//\\s*.*")) {
          // exclude line comments
          ""
        } else if (line_.contains("/*")) {
          isComment = true
          val arr = line_.split("/\\*")
          if (arr.length > 0) arr(0) else ""
        } else {
          if (line_.contains("*/")) {
            isComment = false
            val arr = line_.split("\\*/")
            if (arr.length > 0) line_ = arr(arr.length - 1) else line_ = ""
          }
          if (isComment) {
            ""
          } else {
            val startCount = line_.count(c => c == '{')
            val endCount = line_.count(c => c == '}')
            if (blockDepth == 0 && startCount > 0) {
              blockDepth = blockDepth + startCount - endCount
              line_.split("\\{")(0)
            } else {
              blockDepth = blockDepth + startCount - endCount
              if (blockDepth < 0) blockDepth = 0
              if (blockDepth == 0) {
                if (endCount > 0) {
                  val arr = line_.split("\\}")
                  if (arr.length > 0) arr(arr.length - 1) else ""
                } else line_
              } else {
                ""
              }
            }
          }
        }
      }
    }).mkString(" ")
  }

}