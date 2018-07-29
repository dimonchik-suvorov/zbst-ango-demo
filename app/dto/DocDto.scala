package dto

import play.api.libs.json.{Json, Reads, Writes}

case class DocDto(id: Option[String], name: String)

object DocDto {
  implicit val writes: Writes[DocDto] = Json.writes[DocDto]

  implicit val reads: Reads[DocDto] = Json.reads[DocDto]

  def from(modelDoc: model.Doc): dto.DocDto = DocDto(Some(modelDoc.id.stringify), modelDoc.name)
}
