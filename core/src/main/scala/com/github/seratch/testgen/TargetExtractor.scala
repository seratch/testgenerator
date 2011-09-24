/*
 * Copyright 2011 Kazuhiro SERA.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package com.github.seratch.testgen

import io.Source
import java.io.File

class TargetExtractor {

  // TODO directory specified

  def extract(path: String): List[Target] = {
    val lines = readLines(path)
    val defOnly = extractDefOnly(lines)
    defOnly.split("package").toList flatMap {
      case eachDefOnly => {
        val fullPackageName = eachDefOnly.trim.split("\\s+").toList.head
        val importList = extractImportList(defOnly)
        val parser = new TargetParser(fullPackageName, importList)
        var defOnlyToParse = eachDefOnly.replaceFirst(fullPackageName, "")
        importList foreach {
          case toImport => defOnlyToParse = defOnlyToParse.replaceAll(
            "\\s*import\\s+" + toImport + "\\s*", "")
        }
        val parserResult = parser.parse(defOnlyToParse)
        parserResult.getOrElse(Nil)
      }
    }
  }

  // TODO configure encoding
  def readLines(path: String): List[String] = Source.fromFile(new File(path), "UTF-8").getLines.toList

  // TODO package block
  def extractDefOnly(lines: List[String]): String = {
    var isComment = false
    var blockDepth = 0
    (lines map {
      line => {
        // mutable line string
        var line_ = line.replaceAll("\\s*:\\s*", ":")
        if (line_.matches("\\s*//\\s*.*")) {
          // line comment
          ""
        } else if (line_.contains("/*")) {
          // comment start
          isComment = true
          val arr = line_.split("/\\*")
          if (arr.length > 0) arr(0) else ""
        } else {
          if (line_.contains("*/")) {
            // comment end
            isComment = false
            val arr = line_.split("\\*/")
            if (arr.length > 0) line_ = arr(arr.length - 1) else line_ = ""
          }
          if (isComment) {
            // inside of comment
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

  def extractImportList(defOnly: String): List[String] = {
    defOnly.split("import\\s+").toList drop (1) map {
      case each => {
        val toImport = each.trim.split("\\s+").toList.head
        // e.g. import java.io.{InputStream, OutputStream}
        if (toImport.contains("{")) toImport.split("\\{").toList.head + "_"
        else toImport
      }
    }
  }

}