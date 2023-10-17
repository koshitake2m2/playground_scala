package com.koshitake2m2.playSample3.di

import com.google.inject.AbstractModule
import net.codingwell.scalaguice.ScalaModule
import org.slf4j.{Logger, LoggerFactory}

class GuiceModule extends AbstractModule with ScalaModule {
  override def configure(): Unit =
    bind[Logger].toInstance(LoggerFactory.getLogger("playSample3"))
}
