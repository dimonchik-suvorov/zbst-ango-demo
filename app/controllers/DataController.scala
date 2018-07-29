package controllers

import javax.inject.{Inject, Singleton}
import play.api.libs.json.{JsArray, Json}
import play.api.mvc._
import services.ElasticService

import scala.concurrent.ExecutionContext

@Singleton
class DataController @Inject()(val cc: ControllerComponents,
                               val elastic: ElasticService)(implicit val ec: ExecutionContext) extends AbstractController(cc) {

  def search: Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    val queryPattern: String = request.getQueryString("pattern") match {
      case Some(pattern) => pattern
      case None => throw new IllegalArgumentException(s"missing required query parameter 'pattern', got ${request.rawQueryString}")
    }
    elastic.search(queryPattern)
      .map(res => Ok(Json.parse(res.body)))
  }

  def schema: Action[AnyContent] = Action.async {
    elastic.getSchema.map(res => Ok(res))
  }

  def bulkUpload: Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    val entries = request.body.asJson.map(data => data.asInstanceOf[JsArray].value).get
    elastic.bulkUpload(entries)
      .map(response => Status(response.status)(Json.parse(response.body)))
  }
}
