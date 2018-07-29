package application

import org.specs2.matcher.ShouldMatchers
import play.api.libs.json.{JsString, JsValue, Json}
import play.api.libs.ws.{WSClient, WSResponse}
import play.api.test.{PlaySpecification, WithServer}

// TODO: currently these tests require mongo and elastic running locally on default ports
class ApplicationSpec extends PlaySpecification with ShouldMatchers {
  sequential

  "With Document endpoints it" should {

    "be possible to create doc" in new WithServer {
      val ws: WSClient = app.injector.instanceOf(classOf[WSClient])

      private val creationResponse: WSResponse = await(ws.url(url).post(Json.parse("""{"name": "hello darling"}""")))

      creationResponse.status must equalTo(CREATED)
    }

    "be possible to search by name" in new WithServer {
      val ws: WSClient = app.injector.instanceOf(classOf[WSClient])
      await(ws.url(url).post(Json.parse("""{"name": "hello baby"}""")))
      await(ws.url(url).post(Json.parse("""{"name": "hello darling"}""")))

      private val searchResponse: WSResponse = await(ws.url(s"$url?name=hello").get())

      searchResponse.status must equalTo(OK)
      Json.parse(searchResponse.body) \\ "_source" map (_ \ "name" get) must contain[JsValue](JsString("hello darling"), JsString("hello baby"))
    }
  }

  private def url: String = {
    s"http://localhost:$testServerPort/docs"
  }

  private def value(response: WSResponse, field: String): JsValue = {
    Json.parse(response.body) \ field get
  }
}