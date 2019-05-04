package v1.songs.models

import play.api.libs.json._

case class Song (id: Option[Long], title: String, composer: String, language: String)

object Song {
  implicit val songWrites = Json.writes[Song]
  implicit val songReads = Json.reads[Song]
}
