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
import v1.songs.models.{Song, SongSummary, SongRepository}

import scala.concurrent.{ExecutionContext, Future}

class SongRouterSpec extends PlaySpec with GuiceOneAppPerTest {

  override def fakeApplication(): Application = new GuiceApplicationBuilder()
    .bindings(bind[SongRepository].to[MockSongRepository]).build()

  "SongRouter" should {
    "return the right number of songs" in {
      val request = FakeRequest(GET, "/v1/songs").withHeaders(HOST -> "localhost:9000").withCSRFToken
      val home:Future[Result] = route(app, request).get

      val songs: Seq[SongSummary] = Json.fromJson[Seq[SongSummary]](contentAsJson(home)).get
      songs.length mustBe 1
      songs(0) mustBe new SongSummary(Some(1), "Thulasidhala", "maayamalavagowla", "Thyagaraaja")
    }

    "add a song" in {
      val request = FakeRequest(POST, "/v1/songs").
        withJsonBody(Json.toJson[Song](
          new Song(None, "Vaathapi", "Muthuswami Dikshitar", "Sanskrit",
            Some("Hamsadhwani"), Some("Aadi"), Some("Vaathapi Ganapathim Bhajeham+ Vaaranaasham vara pradam")))).
        withHeaders(HOST -> "localhost:9000").
        withHeaders(CONTENT_TYPE -> "application/json").withCSRFToken

      val home: Future[Result] = route(app, request).get
      val song: Song = Json.fromJson[Song](contentAsJson(home)).get
      song.title mustBe "Vaathapi"
      song.raagam mustBe Some("Hamsadhwani")
      song.id.get mustBe 1L
    }

    "search for a song by title" in {
      val request = FakeRequest(GET, "/v1/songs/search?q=Rama").
        withHeaders(HOST -> "localhost:9000").withCSRFToken
      val home:Future[Result] = route(app, request).get

      val songs: Seq[SongSummary] = Json.fromJson[Seq[SongSummary]](contentAsJson(home)).get
      songs.length mustBe 3
      songs.foreach(s => {
        s.title.contains("Rama") mustBe true
      })
    }

    "get a song by id" in {
      val request = FakeRequest(GET, "/v1/songs/10").
        withHeaders(HOST -> "localhost:9000").withCSRFToken

      val home: Future[Result] = route(app, request).get

      val song: Song = Json.fromJson[Song](contentAsJson(home)).get
      song.title mustBe "Thulasidhala"
      song.id.get mustBe 10
    }

    "non existent song should give a 404" in {
      val request = FakeRequest(GET, "/v1/songs/0").
        withHeaders(HOST -> "localhost:9000").withCSRFToken

      val home: Future[Result] = route(app, request).get
      status(home) mustBe 404
    }
  }

}

class MockSongRepository @Inject()(@NamedDatabase("tadvidya") dbConfigProvider: DatabaseConfigProvider)
                                  (implicit ec: ExecutionContext) extends SongRepository(dbConfigProvider) {
  override def list(): Future[Seq[SongSummary]] = Future {
    Seq(new SongSummary(Some(1), "Thulasidhala", "maayamalavagowla", "Thyagaraaja"))
  }

  override def findById(id: Long): Future[Option[Song]] = Future {
    id match {
      case 0 => None
      case _ => Some(new Song(Some(id), "Thulasidhala", "Thyagaraja", "Telugu",
        Some("Mayamalavagowla"), Some("Aadi"), Some("Thulasidhala mulache santhoshamuga+ Poojinthu")))
    }
  }

  override def create(title: String, composer: String, language: String,
                      raagam: Option[String], thalam: Option[String], lyrics: Option[String]): Future[Song] =
    Future {
    new Song(Some(1), title, composer, language, raagam, thalam, lyrics)
  }

  override def find(query: String): Future[Seq[SongSummary]] = Future {
    Seq(new SongSummary(Some(1), s"prefix${query}", "raagam", "composer"),
      new SongSummary(Some(2), s"prefix${query}suffix", "raagam", "composer"),
      new SongSummary(Some(3), s"${query}suffix", "raagam", "composer"))
  }
}

