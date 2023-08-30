package com.koshitake2m2.playSample.di

import cats.effect.IO
import com.google.inject.AbstractModule
import com.koshitake2m2.playSample.batch.QuartzSample
import io.chrisdavenport.log4cats.{Logger => CatsLogger}
import io.chrisdavenport.log4cats.slf4j.Slf4jLogger
import net.codingwell.scalaguice.ScalaModule
import org.slf4j.{Logger, LoggerFactory}

class GuiceModule extends AbstractModule with ScalaModule {
  override def configure(): Unit = {
    val unsafeLogger: CatsLogger[IO] = Slf4jLogger.getLogger[IO]
    bind[CatsLogger[IO]].toInstance(unsafeLogger)
    bind[Logger].toInstance(LoggerFactory.getLogger("playSample"))
    bind[QuartzSample].asEagerSingleton()
  }
}
