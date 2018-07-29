package application

import org.specs2.matcher.ShouldMatchers
import play.api.libs.json.{JsString, Json}
import play.api.libs.ws.{WSClient, WSResponse}
import play.api.test.{PlaySpecification, WithServer}

// TODO: currently these tests require mongo and elastic running locally on default ports
class SearchTest extends PlaySpecification with ShouldMatchers {
  sequential

  "Search" should {

    "contains 'Simpson'" in new WithServer {
      val ws: WSClient = app.injector.instanceOf(classOf[WSClient])

      println("Search: " + searchUrl("Simpson"))
      private val searchResponse: WSResponse = await(ws.url(searchUrl("Simpson")).get())

      searchResponse.status must equalTo(OK)
      val body = searchResponse.body
      println("Response: " + body)
      Json.parse(body) \\ "firstname" contains JsString("Simpson")
      println("OK")
    }
  }

  private def searchUrl(pattern: String): String = {
    s"http://localhost:$testServerPort/search?pattern=$pattern"
  }

  private def payload: String = {
    """{"index":{"_id":"1"}}
    {"account_number":1,"balance":39225,"firstname":"Amber","lastname":"Duke","age":32,"gender":"M","address":"880 Holmes Lane","employer":"Pyrami","email":"amberduke@pyrami.com","city":"Brogan","state":"IL"}
    {"index":{"_id":"6"}}
    {"account_number":6,"balance":5686,"firstname":"Hattie","lastname":"Bond","age":36,"gender":"M","address":"671 Bristol Street","employer":"Netagy","email":"hattiebond@netagy.com","city":"Dante","state":"TN"}"""
  }
}
