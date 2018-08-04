package application

import java.io.File
import java.nio.file.Files

import akka.stream.scaladsl.{FileIO, Source}
import akka.util.ByteString
import org.specs2.matcher.ShouldMatchers
import play.api.libs.json.Json
import play.api.libs.ws.{WSClient, WSResponse}
import play.api.mvc.MultipartFormData
import play.api.test.{PlaySpecification, WithServer}

// TODO: currently these tests require mongo and elastic running locally on default ports
class UploadTest extends PlaySpecification with ShouldMatchers {
  sequential

  "Upload" should {

    "be 200 OK" in new WithServer {
      val ws: WSClient = app.injector.instanceOf(classOf[WSClient])

      println("Upload url: " + uploadUrl)
      private val searchResponse: WSResponse = await(ws.url(uploadUrl).post(Json.parse(payload)))

      searchResponse.status must equalTo(OK)
      println("OK")
    }
  }

  "File upload" should {
    "upload a file successfully" in new WithServer {
      val ws: WSClient = app.injector.instanceOf(classOf[WSClient])

      println("Upload url: " + uploadFileUrl)

      val tmpFile = java.io.File.createTempFile("uploadTest", "json")
      tmpFile.deleteOnExit()
      Files.write(tmpFile.toPath, payload.getBytes())
      private val response: WSResponse = await(ws.url(uploadFileUrl).post(postSource(tmpFile)))

      response.status must equalTo(OK)
      println("OK")
    }
  }

  def postSource(tmpFile: File): Source[MultipartFormData.Part[Source[ByteString, _]], _] = {
    import play.api.mvc.MultipartFormData._
    Source(FilePart("name", "data.json", Option("text/plain"),
      FileIO.fromPath(tmpFile.toPath)) :: DataPart("key", "value") :: List())
  }

  private def uploadUrl: String = {
    s"http://localhost:$testServerPort/upload"
  }

  private def uploadFileUrl: String = {
    s"http://localhost:$testServerPort/upload-file"
  }

  private def payload: String = {
    """[
    |{"account_number":1,"balance":39225,"firstname":"Amber","lastname":"Duke","age":32,"gender":"M","address":"880 Holmes Lane","employer":"Pyrami","email":"amberduke@pyrami.com","city":"Brogan","state":"IL"},
    |{"account_number":6,"balance":5686,"firstname":"Hattie","lastname":"Bond","age":36,"gender":"M","address":"671 Bristol Street","employer":"Netagy","email":"hattiebond@netagy.com","city":"Dante","state":"TN"}
    ]""".stripMargin
  }
}
