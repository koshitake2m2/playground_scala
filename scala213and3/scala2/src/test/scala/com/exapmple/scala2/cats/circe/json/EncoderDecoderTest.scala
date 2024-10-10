package com.exapmple.scala2.cats.circe.json

import com.example.scala2.cats.circe.json.EncoderDecoder
import io.circe.generic.auto._
import io.circe.parser.decode
import io.circe.syntax.EncoderOps
import org.scalatest.wordspec.AnyWordSpec

class EncoderDecoderTest extends AnyWordSpec with EncoderDecoder {
  "AnyVal" should {
    import AnyValFixture._
    "encode" in {
      assert(personA.asJson.noSpaces == personAJsonStr)
    }
    "decode" in {
      assert(decode[Person](personAJsonStr) == Right(personA))
    }
  }

}

object AnyValFixture {

  case class Name(value: String) extends AnyVal
  case class Age(value: Int) extends AnyVal
  case class Person(name: Name, age: Age)

  val personA = Person(
    name = Name("koshi"),
    age = Age(25)
  )
  val personAJsonStr = """{"name":"koshi","age":25}"""
}
