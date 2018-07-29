package services

import javax.inject.{Inject, Singleton}
import model.Doc
import play.api.http.HttpVerbs
import play.api.libs.json.Json
import play.api.libs.ws.{WSClient, WSResponse}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ElasticService @Inject()(val ws: WSClient)(implicit val ec: ExecutionContext) {

  private val esDocRoot: String = "http://elasticsearch:9200/docs/_doc"

  def send(enrichedWithId: Doc): Future[WSResponse] = {
    ws.url(esDocRoot).post(Json.toJson(dto.DocDto.from(enrichedWithId)))
  }

  def search(namePattern: String): Future[WSResponse] = {
    ws.url(s"$esDocRoot/_search")
      .withMethod(HttpVerbs.GET)
      .withBody(Json.parse(
        s"""
           |{
           |    "query" : {
           |        "term" : { "name" : "$namePattern" }
           |    }
           |}
        """.stripMargin))
      .execute()
  }
}
