package com.example.scala2.cats.core

import cats.implicits._

object EitherMain extends App {
  val e = Either.catchNonFatal(1 / 0)
  println(e)
}
