package v1.songs

import javax.inject.Inject
import play.api.routing.Router.Routes
import play.api.routing.SimpleRouter
import play.api.routing.sird._

class SongRouter @Inject()(controller: SongController) extends SimpleRouter {

  override def routes: Routes = {
    case GET(p"/") =>
      controller.listSongs
//    case POST(p"/") =>
//      controller.addSong
  }

}
