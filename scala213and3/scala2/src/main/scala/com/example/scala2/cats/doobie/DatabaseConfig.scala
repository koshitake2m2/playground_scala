package com.example.scala2.cats.doobie

import cats.effect.{Async, Blocker, ContextShift, Resource}
import doobie.hikari.HikariTransactor
import doobie.util.ExecutionContexts
import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder

case class DatabaseConfig(
    driver: String,
    url: String,
    user: String,
    password: String,
    connectionPoolSize: Int
) {
  def transactor[F[_]: Async: ContextShift]: Resource[F, HikariTransactor[F]] =
    for {
      connectEC <- ExecutionContexts.fixedThreadPool[F](connectionPoolSize)
      transactEC <- ExecutionContexts.cachedThreadPool[F]
      transactor <-
        HikariTransactor
          .newHikariTransactor[F](
            driver,
            url,
            user,
            password,
            connectEC,
            Blocker.liftExecutionContext(transactEC)
          )
    } yield transactor
}

object DatabaseConfig {
  implicit val dataBaseDecoder: Decoder[DatabaseConfig] = deriveDecoder
}
