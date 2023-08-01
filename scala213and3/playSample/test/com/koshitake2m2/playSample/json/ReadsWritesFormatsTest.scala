package com.koshitake2m2.playSample.json

import org.scalatest.wordspec.AnyWordSpec
import play.api.libs.json.Format.GenericFormat
import play.api.libs.json.OFormat.oFormatFromReadsAndOWrites
import play.api.libs.json.{Format, JsResult, JsSuccess, JsValue, Json, OFormat, OWrites, Reads, Writes}
import shapeless.{Lazy, Unwrapped}

class ReadsWritesFormatsTest extends AnyWordSpec {
  import Fixture._
  "Writes" should {
    "シンプルなクラス" in {
      implicit val formatName = Json.format[Name]
      implicit val formatAge = Json.format[Age]
      implicit val formatPerson = Json.format[Person]

      // {"name":{"value":"koshi"},"age":{"value":25}}
      val j = Json.toJson(personA)
      println(j.toString())
    }
    "AnyValをunwrap" in {
      implicit val formatName = Json.valueFormat[Name]
      implicit val formatAge = Json.valueFormat[Age]
      implicit val formatPerson = Json.format[Person]

      // {"name":"koshi","age":25}
      val j = Json.toJson(personA)
      println(j.toString())
    }
    "lazy val かつ 型を明示的に指定すれば, Personを先に定義しても問題ない" in {
      implicit lazy val formatPerson: OFormat[Person] = Json.format[Person]
      implicit lazy val formatName: Format[Name] = Json.valueFormat[Name]
      implicit lazy val formatAge: Format[Age] = Json.valueFormat[Age]

      val j = Json.toJson(personA)
      println(j.toString())
    }
    "AnyValを継承したクラスのwrites" in {
      // circeを参考に
      // ref: https://github.com/circe/circe/issues/1442

      // 謎に順番が大事. formatPersonをlazy valにして先に書き, writesAnyValを後に書く
      implicit lazy val formatPerson: OWrites[Person] = Json.writes[Person]
      implicit def writesAnyVal[W <: AnyVal, U](implicit
          unwrapped: Unwrapped.Aux[W, U],
          w: Writes[U]
      ): Writes[W] =
        new Writes[W] {
          override def writes(o: W): JsValue = w.writes(unwrapped.unwrap(o))
        }
      val j = Json.toJson(personA)
      println(j.toString())
    }
  }
  "reads" should {
    "シンプルなクラス" in {
      implicit val formatName = Json.format[Name]
      implicit val formatAge = Json.format[Age]
      implicit val formatPerson = Json.format[Person]

      val jsonStr = """{"name":{"value":"koshi"},"age":{"value":25}}"""
      val personA = Json.fromJson[Person](Json.parse(jsonStr))

      println(personA.toString())
    }
    "AnyValをunwrap" in {
      implicit val formatName = Json.valueFormat[Name]
      implicit val formatAge = Json.valueFormat[Age]
      implicit val formatPerson = Json.format[Person]

      val jsonStr = """{"name":"koshi","age":25}"""
      val personA = Json.fromJson[Person](Json.parse(jsonStr))

      println(personA.toString())
    }
    "lazy val かつ 型を明示的に指定すれば, Personを先に定義しても問題ない" in {
      implicit lazy val formatPerson: OFormat[Person] = Json.format[Person]
      implicit lazy val formatName: Format[Name] = Json.valueFormat[Name]
      implicit lazy val formatAge: Format[Age] = Json.valueFormat[Age]

      val jsonStr = """{"name":"koshi","age":25}"""
      val personA = Json.fromJson[Person](Json.parse(jsonStr))

      println(personA.toString())
    }
    "AnyValを継承したクラスのreads" in {
      // circeを参考に
      // ref: https://github.com/circe/circe/issues/1442

      // 謎に順番が大事. formatPersonをlazy valにすれば先に書ける
      implicit def readsAnyVal[W <: AnyVal, U](implicit unwrapped: Unwrapped.Aux[W, U], r: Reads[U]): Reads[W] =
        new Reads[W] {
          override def reads(json: JsValue): JsResult[W] = r.reads(json).map(unwrapped.wrap)
        }
      implicit val formatPerson: Reads[Person] = Json.reads[Person]

      val jsonStr = """{"name":"koshi","age":25}"""
      val personA = Json.fromJson[Person](Json.parse(jsonStr))

      println(personA.toString())
    }
  }
}

object Fixture {

  case class Name(value: String) extends AnyVal
  case class Age(value: Int) extends AnyVal
  case class Person(name: Name, age: Age)

  val personA = Person(
    name = Name("koshi"),
    age = Age(25)
  )
}

object SimpleFormat {}
