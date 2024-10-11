package com.exapmple.scala2.cats.circe.json

import com.example.scala2.cats.circe.json.AnyEncoderDecoder
import io.circe.{Decoder, Encoder, Json}
import io.circe.syntax._
import io.circe.generic.auto._
import io.circe.generic.extras.Configuration
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.parser.decode
import io.circe.syntax.EncoderOps
import org.scalatest.wordspec.AnyWordSpec
import shapeless.Unwrapped

class PrimitiveWrapperEncoderDecoderTest extends AnyWordSpec with AnyEncoderDecoder {
  import PrimitiveWrapperFixture._
  "AnyVal" should {
    "encode" in {
      assert(personA.asJson.noSpaces == personAJsonStrWithValue)
      assert(personA.asJson.noSpaces != personAJsonStr)
    }
    "decode" in {
      assert(decode[Person](personAJsonStrWithValue) == Right(personA))
      assert(decode[Person](personAJsonStr) != Right(personA))
    }
  }

}

object PrimitiveWrapperFixture {
  trait PrimitiveWrapper[A] {
    val value: A
  }

  implicit val config: Configuration = Configuration.default

  final case class Name(value: String) extends PrimitiveWrapper[String]
  case class Age(value: Int) extends PrimitiveWrapper[Int]
  case class Person(name: Name, age: Age)

  // NOTE: うまくいかない...
  // おそらく, PrimitiveWrapperではなくNameやAgeとして型定義してないと, そもそもEncoderを見つけられない.

//  implicit def primitiveWrapperEncoder[A, W <: PrimitiveWrapper[A]](implicit
////      V: Unwrapped.Aux[W, A],
//      encoder: Encoder[A]
//  ): Encoder[PrimitiveWrapper[A]] =
//    Encoder.instance(_.value.asJson)
//    Encoder.instance {
//      _.value match {
//        case a: String => a.asJson
//        case i: Int => i.asJson
//      }
//    }
//    encoder.contramap(V.unwrap)

//  implicit val personEncoder: Encoder[Person] = deriveEncoder
//
//  implicit def encoder[A, W <: PrimitiveWrapper[A], U](implicit
//      V: Unwrapped.Aux[W, U],
//      encoder: Encoder[U]
//  ): Encoder[W] =
//    encoder.contramap(V.unwrap)

  val personA = Person(
    name = Name("koshi"),
    age = Age(25)
  )
  val personAJsonStr = """{"name":"koshi","age":25}"""
  val personAJsonStrWithValue = """{"name":{"value":"koshi"},"age":{"value":25}}"""
}
