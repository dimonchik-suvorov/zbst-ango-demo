package controllers

import javax.inject.{Inject, Singleton}
import play.api.libs.json.Json
import play.api.mvc._
import services.ElasticService

import scala.concurrent.ExecutionContext

@Singleton
class DataController @Inject()(val cc: ControllerComponents,
                               val elastic: ElasticService)(implicit val ec: ExecutionContext) extends AbstractController(cc) {

  def search: Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    val pattern: String = request.getQueryString("name") match {
      case Some(name) => name
      case None => throw new IllegalArgumentException(s"missing required query parameter 'name', got ${request.rawQueryString}")
    }
    elastic.search(pattern)
      .map(res => Ok(Json.parse(res.body)))
  }
}
