package controllers

import dto.DocDto
import javax.inject.{Inject, Singleton}
import play.api.libs.json.{JsPath, Json, JsonValidationError}
import play.api.mvc._
import services.ElasticService

import scala.concurrent.ExecutionContext

@Singleton
class DocController @Inject()(val cc: ControllerComponents,
                              val elastic: ElasticService)(implicit val ec: ExecutionContext) extends AbstractController(cc) {

  def create: Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    val dto: DocDto = Json.fromJson[DocDto](request.body.asJson.get).asEither match {
      case Right(value) => value
      case Left(errs: Seq[(JsPath, Seq[JsonValidationError])]) =>
        throw new IllegalArgumentException(s"Bad request: ${errs.last}")
    }

    elastic.send(dto).map(ignored => Created)
  }

  def search: Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    val namePattern: String = request.getQueryString("name") match {
      case Some(name) => name
      case None => throw new IllegalArgumentException(s"missing required query parameter 'name', got ${request.rawQueryString}")
    }
    elastic.search(namePattern)
      .map(res => Ok(Json.parse(res.body)))
  }
}
