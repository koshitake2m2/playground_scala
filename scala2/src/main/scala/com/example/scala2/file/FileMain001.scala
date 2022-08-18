package com.example.scala2.file

import scala.io.Source
import java.io.PrintWriter

object FileMain001 extends App {

  /** ファイルの読み込み
    */
  // プロジェクトルート
  val s = Source.fromFile("README.md")
  s.getLines.foreach(l => println(l))
  s.close()

  // 絶対パス
  val s2 = Source.fromFile("/etc/hosts")
  s2.getLines.foreach(l => println(l))
  s2.close()

  /** ファイルの書き込み
    */
  // プロジェクトルート
  val f = new PrintWriter("scala2/src/main/scala/com/example/scala2/file/hello.md")
  f.write("""hello
    |world
    |""".stripMargin)
  f.write("wow") // 追記
  f.close()

  // 絶対パス
  val f2 = new PrintWriter("/tmp/hello2.md")
  f2.write("""こんにちは
    |world
    |""".stripMargin)
  f2.close()
}
