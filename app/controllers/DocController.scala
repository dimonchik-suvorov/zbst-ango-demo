package controllers

import dao.DocDao
import dto.DocDto
import javax.inject.{Inject, Singleton}
import model.Doc
import play.api.libs.json.{JsPath, Json, JsonValidationError}
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents, Request}
import services.ElasticService

import scala.concurrent.ExecutionContext

@Singleton
class DocController @Inject()(val cc: ControllerComponents,
                              val dao: DocDao,
                              val elastic: ElasticService)(implicit val ec: ExecutionContext) extends AbstractController(cc) {

  def create: Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    val dto: DocDto = Json.fromJson[DocDto](request.body.asJson.get).asEither match {
      case Right(value) => value
      case Left(errs: Seq[(JsPath, Seq[JsonValidationError])]) =>
        throw new IllegalArgumentException(s"Bad request: ${errs.last}")
    }

    val saved: Doc = Doc.from(dto)

    dao.create(saved)
      .flatMap(ignored => elastic.send(saved))
      .map(ignored => Created(Json.toJson(DocDto.from(saved))))
  }

  def get(id: String): Action[AnyContent] = Action.async {
    dao.read(id)
      .map({
        case Some(doc) => Ok(Json.toJson(DocDto.from(doc)))
        case None => throw new IllegalArgumentException(s"Not found: $id")
      })
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
