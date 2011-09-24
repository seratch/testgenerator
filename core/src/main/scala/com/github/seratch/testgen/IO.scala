package com.github.seratch.testgen

import java.io.{InputStreamReader, BufferedReader, InputStream}

object IO {

  def using[CLOSABLE <: {def close() : Unit}, RETURNED](resource: CLOSABLE)(func: CLOSABLE => RETURNED): RETURNED = {
    try {
      func(resource)
    } finally {
      resource match {
        case null =>
        case _ => try resource.close() catch {
          case _ =>
        }
      }
    }
  }

}