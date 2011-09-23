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
        writer => writer.write(sourceCode)
      }
    }
  }

}