package com.koshitake2m2.playSample

import com.google.inject.{Inject, Singleton}
import play.api.libs.json.{Json, OWrites}
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}

import java.io.{DataOutputStream, FileOutputStream}
import java.nio.file.Files
import java.util.concurrent.TimeUnit
import scala.concurrent.ExecutionContext

@Singleton
class FileController @Inject() (
    controllerComponents: ControllerComponents
)(implicit
    ec: ExecutionContext
) extends AbstractController(controllerComponents) {

  case class HelloWorld(hello: String, year: Int)
  implicit val helloWorldWrites: OWrites[HelloWorld] = Json.writes[HelloWorld]

  def download: Action[AnyContent] = Action { _ =>
    val tempFile = Files.createTempFile("hello", ".csv").toFile
    val dataOutputStream = new DataOutputStream(new FileOutputStream(tempFile))
    dataOutputStream.writeChars("abc,def,ghi\n")
    dataOutputStream.writeChars("123,456,789\n")
    dataOutputStream.close()

    // delay
    TimeUnit.SECONDS.sleep(1)

    Ok.sendFile(tempFile)
  }

}
