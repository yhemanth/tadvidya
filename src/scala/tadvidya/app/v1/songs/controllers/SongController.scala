package v1.songs.controllers

import javax.inject.Inject
import play.api.Logger
import play.api.http.FileMimeTypes
import play.api.i18n.{Langs, MessagesApi}
import play.api.libs.json.Json
import play.api.mvc._
import utils.RequestMarkerContext
import v1.songs.models.{Song, SongRepository}

import scala.concurrent.{ExecutionContext, Future}

class SongController @Inject()(repo: SongRepository,
                               scc: SongControllerComponents) (implicit ec: ExecutionContext)
                              extends BaseController
                              with RequestMarkerContext {
  override protected def controllerComponents: ControllerComponents = scc

  val logger = Logger(this.getClass)

  def SongAction: SongActionBuilder = scc.songActionBuilder

  def listSongs: Action[AnyContent] = SongAction.async { implicit request =>
    logger.trace("Inside listSongs")
    repo.list().map { songs
      => Ok(Json.toJson(songs))
    }
  }

  def findSongById(id: Long): Action[AnyContent] = SongAction.async { implicit request =>
    repo.findById(id).map {
      case Some(song) => Ok(Json.toJson(song))
      case None => NotFound(s"No song with ID $id")
    }
  }


  def addSong: Action[AnyContent] = SongAction.async { implicit request =>
    logger.trace("Inside addSong")
    val songAsJson = request.body.asJson.get
    val song = songAsJson.as[Song]
    logger.info("Received song: $song")
    repo.create(song.title, song.composer, song.language).map {
      s => Ok(Json.toJson(s))
    }
  }

  def findSongs(query: String): Handler = SongAction.async { implicit request =>
    repo.find(query).map { songs
      => Ok(Json.toJson(songs))
    }
  }

}

/**
  * Packages up the component dependencies for the song controller.
  *
  * This is a good way to minimize the surface area exposed to the controller, so the
  * controller only has to have one thing injected.
  */
case class SongControllerComponents @Inject()(
                                               songActionBuilder: SongActionBuilder,
                                               actionBuilder: DefaultActionBuilder,
                                               parsers: PlayBodyParsers,
                                               messagesApi: MessagesApi,
                                               langs: Langs,
                                               fileMimeTypes: FileMimeTypes,
                                               executionContext: scala.concurrent.ExecutionContext)

  extends ControllerComponents
