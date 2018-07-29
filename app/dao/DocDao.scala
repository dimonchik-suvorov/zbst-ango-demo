package dao

import javax.inject.{Inject, Singleton}
import model.Doc
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.api.commands.WriteResult
import reactivemongo.bson.{BSONDocument, BSONObjectID}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class DocDao @Inject()(private val reactiveMongoApi: ReactiveMongoApi)(implicit val context: ExecutionContext) {
  val docCollection: Future[BSONCollection] = {
    reactiveMongoApi.database.map(_.collection[BSONCollection]("docs"))
  }

  def execute[V](action: BSONCollection => Future[V]): Future[V] = {
    docCollection.flatMap(collection => action(collection))
  }

  def create(doc: Doc): Future[WriteResult] = execute(_.insert(doc))

  def read(id: String): Future[Option[Doc]] = execute(_.find(BSONDocument("_id" -> BSONObjectID.parse(id).get)).one[Doc])
}
