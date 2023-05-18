package com.koshitake2m2.playSample.di

import cats.Monad
import cats.effect.IO
import com.google.inject.AbstractModule
import io.chrisdavenport.log4cats.Logger
import io.chrisdavenport.log4cats.slf4j.Slf4jLogger
import net.codingwell.scalaguice.ScalaModule

import java.io.File

class GuiceModule extends AbstractModule with ScalaModule {
  override def configure(): Unit = {
    val unsafeLogger: Logger[IO] = Slf4jLogger.getLogger[IO]
    bind[Logger[IO]].toInstance(unsafeLogger)

  }
}
