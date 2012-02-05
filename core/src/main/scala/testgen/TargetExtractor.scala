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
package testgen

import io.Source
import java.io.File

class TargetExtractor(val config: Config) {

  val debugLog = new DebugLog(config)

  def extract(pathOrPackage: String): List[Target] = {
    val path_ = pathOrPackage.replaceAll("\\.", "/").replaceFirst("/scala$", ".scala")
    if (!path_.startsWith(config.srcDir)) {
      extractAllFilesRecursively(new File(config.srcDir + "/" + path_))
    } else {
      extractAllFilesRecursively(new File(path_))
    }
  }

  def extractAllFilesRecursively(file: File): List[Target] = {
    var file_ = file
    val fileWithDotScala = new File(file.getPath + ".scala")
    if (!file.exists && fileWithDotScala.exists) {
      file_ = fileWithDotScala
    }
    if (file_.isDirectory) {
      file_.listFiles.toList flatMap {
        case child if child.isDirectory => extractAllFilesRecursively(child)
        case child => readFileAndExtractTargets(child)
      }
    } else {
      readFileAndExtractTargets(file_)
    }
  }

  def readFileAndExtractTargets(file: File): List[Target] = {
    val lines = readLines(file.getPath)
    val (defOnly, importedList) = getDefOnlyAndImportedList(lines)
    extractFromDefOnly(defOnly, importedList)
  }

  private[testgen] def extractFromDefOnly(defOnly: String, importedList: List[String]): List[Target] = {
    defOnly.split("package").toList flatMap {
      case eachDefOnly => {
        val fullPackageName = eachDefOnly.trim.split("\\s+").toList.head
        val parser = new TargetParser(fullPackageName, importedList)
        val defOnlyToParse = eachDefOnly.replaceFirst(fullPackageName, "")
        parser.parse(defOnlyToParse).getOrElse(Nil)
      }
    }
  }

  private[testgen] def readLines(path: String, encoding: String = config.encoding): List[String] = {
    Source.fromFile(new File(path), encoding).getLines.toList
  }

  private[testgen] def getDefOnlyAndImportedList(lines: List[String]): (String, List[String]) = {

    debugLog.ifDebug("TargetExtractor:lines -> \"" + lines.mkString("\n") + "\"")

    object Pattern {
      val importedType = java.util.regex.Pattern.compile("import\\s+([\\w\\.]+)")
    }

    val importedList = new collection.mutable.ListBuffer[String]
    var isComment = false
    var blockDepth = 0
    val defOnly = (lines map {
      line =>
        {
          // mutable line string
          var line_ = line
          if (line_.matches("\\s*import\\s+.+$")) {
            val matcher = Pattern.importedType.matcher(line_)
            if (matcher.find) {
              val importedType = {
                if (matcher.group(1).endsWith(".")) matcher.group(1) + "_"
                else matcher.group(1)
              }
              importedList.append(importedType)
              // remove type-import
              line_ = line_.replaceFirst("import\\s+.+$", "")
            }
          }
          // remove semicolon
          line_ = line_.replaceAll(";", "")

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
              if (blockDepth == 0) {
                blockDepth = blockDepth + startCount - endCount
                if (blockDepth < 0) blockDepth = 0
                if (startCount == 0 && endCount == 0) {
                  line_
                } else {
                  var prefix = ""
                  if (startCount > 0) {
                    prefix = line_.split("\\{")(0)
                  }
                  var suffix = ""
                  if (endCount > 0) {
                    val arr = line_.split("\\}")
                    suffix = if (arr.length > 1) arr.last else ""
                  }
                  prefix + suffix
                }
              } else {
                blockDepth = blockDepth + startCount - endCount
                if (blockDepth < 0) blockDepth = 0
                ""
              }
            }
          }
        }
    }).mkString(" ")

    debugLog.ifDebug("TargetExtractor:defOnly -> \"" + defOnly + "\"")

    val defOnlyWithoutStringValues = defOnly.replaceAll("\\\\\"", "") // escaped double quartation
      .replaceAll("\"{3}[^\"{3}]+?\"{3}", "\"\"") // here document
      .replaceAll("\"[^\"]*?\"", "\"\"") // string literal

    debugLog.ifDebug("TargetExtractor:defOnlyWithoutStringValues -> \"" + defOnlyWithoutStringValues + "\"")

    (defOnlyWithoutStringValues, importedList.toList)
  }

}