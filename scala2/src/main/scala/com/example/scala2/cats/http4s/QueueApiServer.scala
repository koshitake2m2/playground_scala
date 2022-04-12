package com.example.scala2.cats.http4s

import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits._
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s.HttpRoutes
import org.http4s.circe._
import org.http4s.dsl.io._
import org.http4s.implicits._
import org.http4s.server.blaze.BlazeServerBuilder

import scala.collection.mutable
import scala.concurrent.ExecutionContext.global
import scala.concurrent.duration._

object QueueApiServer extends IOApp {
  private val port = 8888
  private val host = "localhost"

  case class HelloWorld(msg: String, input: String)

  val q: mutable.Queue[String] = mutable.Queue.empty

  val httpRoutes = HttpRoutes.of[IO] {
    // curl 'localhost:8888/hello'
    case GET -> Root / "hello" / str =>
      q.enqueue(str)
      val helloWorld = HelloWorld("Hello!", str)
      Ok(helloWorld.asJson)
  }

  def hello: IO[Unit] = IO.sleep(3.seconds) >> IO {
    Either.catchOnly[NoSuchElementException](q.dequeue()) match {
      case Right(str) => println(s"dequeue: $str")
      case Left(_) => println(s"queue is empty.")
    }
  } >> hello

  def run(args: List[String]): IO[ExitCode] =
    BlazeServerBuilder[IO](global)
      .bindHttp(port, host)
      .withHttpApp(httpRoutes.orNotFound)
      .resource
      .use { _ =>
        hello // *> IO.never
      }
      .as(ExitCode.Success)
}
