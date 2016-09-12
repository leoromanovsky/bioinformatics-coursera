package com.leoromanovsky.bioinformatics

import scala.io.Source

object VibrioCholerae {
  val filename = "data/vibrio_cholerae.txt"
  lazy val code = Source.fromFile(filename).getLines.mkString

  def main(args: Array[String]): Unit = {
    println(s"VibrioCholerae length: ${code.size}")
  }
}
