package services

import javax.inject.{Inject, Singleton}
import play.api.http.HttpVerbs
import play.api.libs.json.Json
import play.api.libs.ws.{WSClient, WSResponse}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ElasticService @Inject()(val ws: WSClient)(implicit val ec: ExecutionContext) {

  // we are using one mapping and data type per instance of app
  private val esDocRoot: String = "http://localhost:9200/data/_doc"

  def search(queryPattern: String): Future[WSResponse] = {
    ws.url(s"$esDocRoot/_search")
      .withMethod(HttpVerbs.GET)
      .withBody(Json.parse(
        s"""
           |{
           |  "query": {
           |    "multi_match" : {
           |      "query": "$queryPattern"
           |    }
           |  }
           |}
        """.stripMargin))
      .execute()
  }
}
