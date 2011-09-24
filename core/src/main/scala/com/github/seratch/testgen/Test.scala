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

import java.io.{OutputStreamWriter, FileOutputStream, File}

case class Test(fullPackageName: String, testClassName: String, sourceCode: String) {

  def createFileIfNotExist(): Unit = {
    val file = new File("src/test/scala/" + fullPackageName.replaceAll("\\.", "/") + "/" + testClassName + ".scala")
    if (file.exists) {
      println(testClassName + " is already in being.")
    } else {
      if (!file.getParentFile.exists) {
        file.getParentFile.mkdir()
      }
      IO.using(new OutputStreamWriter(new FileOutputStream(file))) {
        writer => {
          writer.write(sourceCode)
          println(testClassName + " is created.")
        }
      }
    }
  }

}