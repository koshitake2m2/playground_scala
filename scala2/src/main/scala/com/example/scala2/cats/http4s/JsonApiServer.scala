package com.example.scala2.cats.http4s

import cats.effect.{ExitCode, IO, IOApp}
import org.http4s.HttpRoutes
import org.http4s.dsl.io._
import org.http4s.implicits._
import io.circe.syntax._
import io.circe.generic.auto._
import org.http4s.circe._
import org.http4s.server.blaze.BlazeServerBuilder

import scala.concurrent.ExecutionContext.global

object JsonApiServer extends IOApp {
  private val port = 8888
  private val host = "localhost"

  case class HelloWorld(msg: String, year: Int)

  val httpRoutes = HttpRoutes.of[IO] {
    // curl 'localhost:8888/hello'
    case GET -> Root / "hello" =>
      val helloWorld = HelloWorld("Hello, World!", 2021)
      Ok(helloWorld.asJson)
  }

  def run(args: List[String]): IO[ExitCode] =
    BlazeServerBuilder[IO](global)
      .bindHttp(port, host)
      .withHttpApp(httpRoutes.orNotFound)
      .resource
      .use(_ => IO.never)
      .as(ExitCode.Success)
}
