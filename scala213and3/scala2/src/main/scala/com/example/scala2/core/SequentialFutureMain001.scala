package com.example.scala2.core

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

object SequentialFutureMain001 extends App {

  def f(index: Int): Future[Unit] =
    Future {
      println(s"start: $index")
      Thread.sleep(100)
      println(s"end: $index")
    }

  def ff(): Future[Unit] =
    (1 to 10)
      .foldLeft[Future[Unit]](Future.successful(())) { (res, i) =>
        for {
          _ <- res
          _ <- f(i)
        } yield ()
      }

  val a = Await.ready(
    ff(),
    Duration.Inf
  )
  println(a)

}
