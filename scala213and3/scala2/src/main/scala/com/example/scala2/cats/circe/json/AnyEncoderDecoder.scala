package com.example.scala2.cats.circe.json

import io.circe.{Decoder, Encoder}
import shapeless.Unwrapped

trait AnyEncoderDecoder {

  implicit def anyValEncoder[W <: AnyVal, U](implicit
      V: Unwrapped.Aux[W, U],
      encoder: Encoder[U]
  ): Encoder[W] =
    encoder.contramap(V.unwrap)

  implicit def anyValDecoder[W <: AnyVal, U](implicit
      V: Unwrapped.Aux[W, U],
      decoder: Decoder[U]
  ): Decoder[W] =
    decoder.map(V.wrap)

}
