package v1.songs.models

import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import play.db.NamedDatabase
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class SongRepository @Inject() (@NamedDatabase("tadvidya") dbConfigProvider: DatabaseConfigProvider)
                               (implicit ec: ExecutionContext) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private class SongsTable(tag: Tag) extends Table[Song](tag, Some("tadvidya"), "songs") {

    def id = column[Option[Long]]("id", O.PrimaryKey, O.AutoInc)
    def title = column[String]("title")
    def composer = column[String]("composer")
    def language = column[String]("language")
    def raagam = column[Option[String]]("raagam")
    def taalam = column[Option[String]]("taalam")
    def lyrics = column[Option[String]]("lyrics")

    def * = (id, title, composer, language, raagam, taalam, lyrics) <> ((Song.apply _).tupled, Song.unapply)
  }

  private val songs = TableQuery[SongsTable]

  def create(title: String, composer: String, language: String,
             raagam: Option[String], taalam: Option[String], lyrics: Option[String]): Future[Song] = db.run {
    (songs.map(s => (s.title, s.composer, s.language, s.raagam, s.taalam, s.lyrics))
      returning songs.map(_.id)
      into ((songProps, id) => Song(id,
                                      songProps._1, songProps._2, songProps._3,
                                      songProps._4, songProps._5, songProps._6))
      ) += ((title, composer, language, raagam, taalam, lyrics))
  }

  def findById(id: Long): Future[Option[Song]] =
    db.run(songs.filter(_.id === id).result.headOption)

  def list(): Future[Seq[SongSummary]] = db.run {
    songs.result.map(_.map(s => SongSummary(s.id, s.title, s.composer, s.language)))
  }

  def find(query: String): Future[Seq[SongSummary]] = db.run {
    songs.filter(_.title like s"%$query%").result.map(_.map(s => SongSummary(s.id, s.title, s.composer, s.language)))
  }

}
