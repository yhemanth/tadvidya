package v1.songs.controllers

import javax.inject.Inject
import play.api.routing.Router.Routes
import play.api.routing.SimpleRouter
import play.api.routing.sird._

class SongRouter @Inject()(controller: SongController) extends SimpleRouter {

  override def routes: Routes = {
    case GET(p"/") =>
      controller.listSongs
    case GET(p"/search" ? q"q=$query") =>
      controller.findSongs(query)
    case GET(p"/$id") =>
      controller.findSongById(id.toLong)
    case POST(p"/") =>
      controller.addSong
  }

}
