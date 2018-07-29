package model

import reactivemongo.bson.{BSONDocument, BSONDocumentReader, BSONDocumentWriter, BSONObjectID}

case class Doc(id: BSONObjectID, name: String)

object Doc {
  implicit object Reader extends BSONDocumentReader[Doc] {
    override def read(bson: BSONDocument): Doc =
      Doc(
        id = bson.getAs[BSONObjectID]("_id").get,
        name = bson.getAs[String]("name").get
      )
  }

  implicit object Writer extends BSONDocumentWriter[Doc] {
    override def write(doc: Doc): BSONDocument =
      BSONDocument(
        "_id" -> doc.id,
        "name" -> doc.name
      )
  }

  def from(dtoDoc: dto.DocDto): Doc =
    Doc(dtoDoc.id.flatMap(BSONObjectID.parse(_).toOption).getOrElse(BSONObjectID.generate()), dtoDoc.name)
}