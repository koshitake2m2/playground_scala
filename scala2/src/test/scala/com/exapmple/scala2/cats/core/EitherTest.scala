package com.exapmple.scala2.cats.core

import cats.implicits._
import org.mockito.scalatest.MockitoSugar
import org.scalatest.wordspec.AnyWordSpec

class EitherTest extends AnyWordSpec with MockitoSugar {
  "Either#catchNonFatal" should {
    "returs Left when 1 / 0" in {
      val e = Either.catchNonFatal(1 / 0)

      assert(e.isLeft)
      assert(e.isInstanceOf[Left[Throwable, Int]])
      assert(e.isInstanceOf[Left[ArithmeticException, Int]])
      assertThrows[ArithmeticException] {
        e.leftMap(e => throw e)
      }
    }
  }
}
