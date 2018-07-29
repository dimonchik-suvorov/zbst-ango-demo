package application

import org.specs2.matcher.ShouldMatchers
import play.api.libs.ws.{WSClient, WSResponse}
import play.api.test.{PlaySpecification, WithServer}

// TODO: currently these tests require mongo and elastic running locally on default ports
class UploadTest extends PlaySpecification with ShouldMatchers {
  sequential

  "Upload" should {

    "be 200 OK" in new WithServer {
      val ws: WSClient = app.injector.instanceOf(classOf[WSClient])

      println("Upload url: " + uploadUrl)
      private val searchResponse: WSResponse = await(ws.url(uploadUrl).post(payload))

      searchResponse.status must equalTo(OK)
      println("OK")
    }
  }

  private def uploadUrl: String = {
    s"http://localhost:$testServerPort/upload"
  }

  private def payload: String = {
    """{"index":{"_id":"1"}}
    {"account_number":1,"balance":39225,"firstname":"Amber","lastname":"Duke","age":32,"gender":"M","address":"880 Holmes Lane","employer":"Pyrami","email":"amberduke@pyrami.com","city":"Brogan","state":"IL"}
    {"index":{"_id":"6"}}
    {"account_number":6,"balance":5686,"firstname":"Hattie","lastname":"Bond","age":36,"gender":"M","address":"671 Bristol Street","employer":"Netagy","email":"hattiebond@netagy.com","city":"Dante","state":"TN"}"""

  }
}
