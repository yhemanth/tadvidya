package v1.songs

import javax.inject.Inject
import play.api.Logger
import play.api.http.FileMimeTypes
import play.api.i18n.{Langs, MessagesApi}
import play.api.libs.json.{Format, Json}
import play.api.mvc._
import utils.RequestMarkerContext

import scala.concurrent.{ExecutionContext, Future}

case class SongResource(
                         id: Option[String],
                         link: Option[String],
                         title: String,
                         composer: String,
                         language: String)

object SongResource {
  /**
    * Mapping to read/write a SongResource out as a JSON value.
    */
  implicit val songResourceWrites = Json.writes[SongResource]
  implicit val songResourceReads = Json.reads[SongResource]
}

class SongController @Inject()(scc: SongControllerComponents) (implicit ec: ExecutionContext)
                              extends BaseController
                              with RequestMarkerContext {

  override protected def controllerComponents: ControllerComponents = scc

  val logger = Logger(this.getClass)

  def SongAction: SongActionBuilder = scc.songActionBuilder

  def listSongs: Action[AnyContent] = SongAction.async { implicit request =>
    logger.trace("Inside listSongs")
    val songs = Future {
      Iterable(new SongResource(Some("1"), Some("http://localhost:9000/v1/songs/1"),
        "Thulasidhala", "Thyagaraja", "Telugu"))
    }
    songs.map {
      s => Ok(Json.toJson(s))
    }
  }

//  def addSong: Action[AnyContent] = SongAction.async { implicit request =>
//    logger.trace("Inside addSong")
//  }

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
