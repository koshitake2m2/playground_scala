package com.koshitake2m2.playSample

import com.google.inject.{Inject, Singleton}
import play.api.libs.Files
import play.api.libs.json.{Format, Json, OFormat}
import play.api.mvc._
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.{GetObjectRequest, PutObjectRequest}

import java.net.URI
import java.nio
import scala.concurrent.ExecutionContext
import scala.jdk.CollectionConverters._
import scala.util.Using
import scala.util.chaining.scalaUtilChainingOps

@Singleton
class S3FileController @Inject() (
    controllerComponents: ControllerComponents
)(implicit
    ec: ExecutionContext
) extends AbstractController(controllerComponents) {
  import S3FileController._

  // ref: https://docs.aws.amazon.com/ja_jp/sdk-for-java/latest/developer-guide/examples-s3-objects.html
  private val s3ClientBuilder = S3Client
    .builder()
    .forcePathStyle(true)
//    .region(Region.of("ap-northeast-1"))
    .endpointOverride(new URI("http://localhost:4567"))
  private val bucketName = "play-sample-bucket"

  def uploadS3: Action[MultipartFormData[Files.TemporaryFile]] = Action(parse.multipartFormData) { implicit request =>
    request.body
      .file("file")
      .map { file =>
        val filename = file.filename
        val contentType = file.contentType
        println(s"filename: $filename")
        println(s"contentType: $contentType")
        // S3ClientがAutoCloseableをimplementしているので, Using.resourceを使うことでcloseしてくれる.
        // また, Using.Releasable[AutoCloseable]が標準で定義されている.
        Using.resource(s3ClientBuilder.build()) { client =>
          val objectKey = "hello.csv"
          val objectRequest = PutObjectRequest.builder
            .bucket(bucketName)
            .key(objectKey)
            .build
          client.putObject(objectRequest, file.ref.toPath)
        }
        Ok(Json.parse("""{"result": "success"}"""))
      }
      .getOrElse {
        BadRequest("File not found")
      }
  }

  def downloadS3: Action[AnyContent] = Action { _ =>
    val tempFile = nio.file.Files.createTempFile("hello", ".csv").toFile

    Using.resource(s3ClientBuilder.build()) { client =>
      val objectKey = "hello.csv"
      val objectRequest = GetObjectRequest.builder.bucket(bucketName).key(objectKey).build
      client.getObject(objectRequest, tempFile.toPath)
    }

    Ok.sendFile(tempFile)
  }

  def bucketList: Action[AnyContent] = Action { _ =>
    val bucketsDto = Using.resource(s3ClientBuilder.build()) { client =>
      val buckets = client.listBuckets().buckets().asScala.toList
      println(buckets)
      buckets.map(b => BucketDto(b.name())).pipe(BucketsDto)
    }

    Ok(Json.toJson(bucketsDto))
  }
}

object S3FileController {
  case class BucketDto(name: String)

  case class BucketsDto(buckets: List[BucketDto])

  implicit val bucketDtoFormat: OFormat[BucketDto] = Json.format[BucketDto]
  implicit val bucketsDtoFormat: OFormat[BucketsDto] = Json.format[BucketsDto]

}
