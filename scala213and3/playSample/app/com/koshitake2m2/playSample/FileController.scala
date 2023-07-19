package com.koshitake2m2.playSample

import com.google.inject.{Inject, Singleton}
import play.api.libs.Files
import play.api.libs.json.{Json, OWrites}
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents, MultipartFormData}

import java.io.{
  BufferedReader,
  DataInputStream,
  DataOutputStream,
  File,
  FileInputStream,
  FileOutputStream,
  InputStreamReader
}
import java.nio
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

  def upload: Action[MultipartFormData[Files.TemporaryFile]] = Action(parse.multipartFormData) { implicit request =>
    request.body
      .file("file")
      .map { file =>
        val filename = file.filename
        val contentType = file.contentType
        println(s"filename: $filename")
        println(s"contentType: $contentType")
        val dateInputStream = new DataInputStream(new FileInputStream(file.ref.toFile))
        val bufferedReader = new BufferedReader(new InputStreamReader(dateInputStream))
        val lines = Iterator.continually(bufferedReader.readLine()).takeWhile(_ != null)
        println("lines: ")
        lines.foreach(println)
        Ok(Json.toJson(lines.toList))
      }
      .getOrElse {
        BadRequest("File not found")
      }
  }

  def download: Action[AnyContent] = Action { _ =>
    val tempFile = nio.file.Files.createTempFile("hello", ".csv").toFile
    val dataOutputStream = new DataOutputStream(new FileOutputStream(tempFile))
    dataOutputStream.writeChars("abc,def,ghi\n")
    dataOutputStream.writeChars("123,456,789\n")
    dataOutputStream.close()

    // delay
    // TimeUnit.SECONDS.sleep(1)

    Ok.sendFile(tempFile)
  }

}
