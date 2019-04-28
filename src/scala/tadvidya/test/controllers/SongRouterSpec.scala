package controllers

import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.libs.json.Json
import play.api.mvc.Result
import play.api.test.CSRFTokenHelper._
import play.api.test.FakeRequest
import play.api.test.Helpers._
import play.api.test.Helpers.{GET, HOST, contentAsJson, route}
import v1.songs.SongResource

import scala.concurrent.Future

class SongRouterSpec extends PlaySpec with GuiceOneAppPerTest {

  "SongRouter" should {
    "return the right number of songs" in {
      val request = FakeRequest(GET, "/v1/songs").withHeaders(HOST -> "localhost:9000").withCSRFToken
      val home:Future[Result] = route(app, request).get

      val songs: Seq[SongResource] = Json.fromJson[Seq[SongResource]](contentAsJson(home)).get
      songs.length mustBe 1
    }

    "add a song" in {
      val request = FakeRequest(POST, "/v1/songs").
        withJsonBody(Json.toJson[SongResource](
          new SongResource(None, None, "Vaathapi", "Muthuswami Dikshitar", "Sanskrit"))).
        withHeaders(HOST -> "localhost:9000").
        withHeaders(CONTENT_TYPE -> "application/json").withCSRFToken

      val home: Future[Result] = route(app, request).get
      val song: SongResource = Json.fromJson[SongResource](contentAsJson(home)).get
      song.title mustBe "Vaathapi"
    }
  }

}
