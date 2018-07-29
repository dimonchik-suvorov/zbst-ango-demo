package application

import org.specs2.matcher.ShouldMatchers
import play.api.libs.json.{JsString, JsValue, Json}
import play.api.libs.ws.{WSClient, WSResponse}
import play.api.test.{PlaySpecification, WithServer}

// TODO: currently these tests require mongo and elastic running locally on default ports
class ApplicationSpec extends PlaySpecification with ShouldMatchers {
  sequential

  "With Document endpoints it" should {

    "be possible to search by pattern" in new WithServer {
      val ws: WSClient = app.injector.instanceOf(classOf[WSClient])
      await(ws.url(url).post(Json.parse("""{"pattern": "hello baby"}""")))
      await(ws.url(url).post(Json.parse("""{"pattern": "hello darling"}""")))

      private val searchResponse: WSResponse = await(ws.url(s"$url?pattern=hello").get())

      searchResponse.status must equalTo(OK)
      Json.parse(searchResponse.body) \\ "_source" map (_ \ "pattern" get) must contain[JsValue](JsString("hello darling"), JsString("hello baby"))
    }
  }

  private def url: String = {
    s"http://localhost:$testServerPort/docs"
  }

  private def value(response: WSResponse, field: String): JsValue = {
    Json.parse(response.body) \ field get
  }
}
