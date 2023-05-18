package com.koshitake2m2.playSample

import com.google.inject.{Inject, Singleton}
import play.api.libs.json.{Json, OWrites}
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}

@Singleton
class HelloController @Inject() (
    controllerComponents: ControllerComponents
) extends AbstractController(controllerComponents) {

  case class HelloWorld(hello: String, year: Int)
  implicit val helloWorldWrites: OWrites[HelloWorld] = Json.writes[HelloWorld]

  def index: Action[AnyContent] = Action { _ =>
    Ok(Json.toJson(HelloWorld("world", 2022)))
  }

}
