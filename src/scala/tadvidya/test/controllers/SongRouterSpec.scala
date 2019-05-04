package controllers

import javax.inject.Inject
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.Application
import play.api.db.slick.DatabaseConfigProvider
import play.api.inject._
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.Json
import play.api.mvc.Result
import play.api.test.CSRFTokenHelper._
import play.api.test.FakeRequest
import play.api.test.Helpers._
import play.api.test.Helpers.{GET, HOST, contentAsJson, route}
import play.db.NamedDatabase
import v1.songs.models.{Song, SongRepository}

import scala.concurrent.{ExecutionContext, Future}

class SongRouterSpec extends PlaySpec with GuiceOneAppPerTest {

  override def fakeApplication(): Application = new GuiceApplicationBuilder()
    .bindings(bind[SongRepository].to[MockSongRepository]).build()

  "SongRouter" should {
    "return the right number of songs" in {
      val request = FakeRequest(GET, "/v1/songs").withHeaders(HOST -> "localhost:9000").withCSRFToken
      val home:Future[Result] = route(app, request).get

      val songs: Seq[Song] = Json.fromJson[Seq[Song]](contentAsJson(home)).get
      songs.length mustBe 1
    }

    "add a song" in {
      val request = FakeRequest(POST, "/v1/songs").
        withJsonBody(Json.toJson[Song](
          new Song(None, "Vaathapi", "Muthuswami Dikshitar", "Sanskrit"))).
        withHeaders(HOST -> "localhost:9000").
        withHeaders(CONTENT_TYPE -> "application/json").withCSRFToken

      val home: Future[Result] = route(app, request).get
      val song: Song = Json.fromJson[Song](contentAsJson(home)).get
      song.title mustBe "Vaathapi"
      song.id.get mustBe 1L
    }
  }

}

class MockSongRepository @Inject()(@NamedDatabase("tadvidya") dbConfigProvider: DatabaseConfigProvider)
                                  (implicit ec: ExecutionContext) extends SongRepository(dbConfigProvider) {
  override def list(): Future[Seq[Song]] = Future {
    Seq(new Song(Some(1), "Thulasidhala", "Thyagaraja", "Telugu"))
  }

  override def create(title: String, composer: String, language: String): Future[Song] = Future {
    new Song(Some(1), title, composer, language)
  }
}

