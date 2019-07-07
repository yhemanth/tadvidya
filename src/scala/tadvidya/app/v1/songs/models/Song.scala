package v1.songs.models

import play.api.libs.json._

case class Song (id: Option[Long],
                 title: String,
                 composer: String,
                 language: String,
                 raagam: Option[String],
                 taalam: Option[String],
                 lyrics: Option[String]
                )

case class SongSummary(id: Option[Long], title: String, composer: String, language: String)

object Song {
  implicit val songWrites = Json.writes[Song]
  implicit val songReads = Json.reads[Song]
}

object SongSummary {
  implicit val songSummaryWrites = Json.writes[SongSummary]
  implicit val songSummaryReads = Json.reads[SongSummary]
}
