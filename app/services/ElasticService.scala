package services

import javax.inject.{Inject, Singleton}

import play.api.http.HttpVerbs
import play.api.libs.json._
import play.api.libs.ws.{WSClient, WSResponse}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ElasticService @Inject()(val ws: WSClient)(implicit val ec: ExecutionContext) {

  // we are using one mapping and data type per instance of app
  private val esDocRoot: String = "http://localhost:9200/data/_doc"

  def getSchema: Future[JsValue] = {
    ws.url(s"$esDocRoot/_mapping")
      .withMethod(HttpVerbs.GET)
      .execute()
      .map(res => transformMappingToSimpleSchema(res))
  }

  private def transformMappingToSimpleSchema(res: WSResponse) = {
    val result: Seq[JsString] = (res.body[JsValue] \ "data" \ "mappings" \ "_doc" \ "properties")
      .get
      .as[JsObject]
      .fields
      .map(_._1)
      .map(JsString)

    JsArray(result)
  }

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
