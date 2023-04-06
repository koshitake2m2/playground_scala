package com.example.scala2.cats.circe
import cats.implicits._
import io.circe.generic.auto._
import io.circe.parser.decode
import io.circe.syntax._
import io.circe.{Decoder, Encoder, Json}
import io.tabmo.json.rules._
import cats.data.Validated._
import io.tabmo.circe.extra.rules.StringRules

object CirceMain001 extends App {
  case class Name(override val toString: String) extends AnyVal
  case class Foo(
      id: Int,
      name: Name
  )
  val foo = Foo(123, Name("name123"))

  println(foo.asJson)

  // Custom Encode
  {
    implicit val encoder: Encoder[Foo] = Encoder.instance { foo =>
      Json.obj(
        "id" -> foo.id.toString.asJson,
        "custom_name" -> foo.name.toString.asJson
      )
    }
    println(foo.asJson)
  }

  // Custom Decode
  {
    implicit val decoder: Decoder[Foo] = Decoder.instance { cursor =>
      for {
        id <- cursor.downField("id").as[Int]
        name <- cursor.downField("name").downField("value").as[String]
      } yield Foo(id, Name(name))
    }
    val jsonString =
      """
        |{
        |  "id": 123,
        |  "name": {
        |    "value": "hello name"
        |  }
        |}
        |""".stripMargin
    val fooOrE = decode[Foo](jsonString)
    println(fooOrE)
  }

  // tombo
  {
    // Custom Rule
    val idRule: Rule[String, Int] = Rule { i =>
      val idOrError = for {
        rawId <- Either.catchNonFatal(i.toInt).leftMap(_ => "数値以外は不正です.")
        _ <- Either.cond(rawId > 0, (), "0以下は不正です.")
      } yield rawId
      idOrError match {
        case Right(value) => Valid(value)
        case Left(value) => Invalid(ValidationError(value))
      }
    }

    implicit val decoder: Decoder[Foo] = Decoder.instance { cursor =>
      for {
        id <- cursor.downField("id").read(idRule)
        name <- cursor.downField("name").downField("value").read(StringRules.notBlank() |+| StringRules.maxLength(6))
      } yield Foo(id, Name(name))
    }

    // sample1
    {
      val jsonString =
        """
          |{
          |  "id": "123",
          |  "name": {
          |    "value": "Name!"
          |  }
          |}
          |""".stripMargin
      val fooOrE = decode[Foo](jsonString)
      println(fooOrE)
    }

    // sample2
    {
      val jsonString =
        """
          |{
          |  "id": "-123",
          |  "name": {
          |    "value": "hello234"
          |  }
          |}
          |""".stripMargin
      val fooOrE = decode[Foo](jsonString)
      println(fooOrE)
    }
  }

}
