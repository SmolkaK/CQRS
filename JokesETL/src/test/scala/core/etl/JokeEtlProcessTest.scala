package core.etl

import akka.http.scaladsl.model.{HttpEntity, HttpResponse, StatusCodes}
import core.db.connections.H2Conn
import core.db.connections.properties.H2DBConnProperties
import core.db.dao.JokeDAO
import core.db.entities.Joke
import core.db.flyway.FlywaySupport
import core.rest.JokeRestParams
import core.rest.clients.JokesRestClient
import core.db.entities.enums.{JokeCategory, JokeType}
import org.scalatest.BeforeAndAfterEach
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.concurrent.duration.{FiniteDuration, SECONDS}
import scala.concurrent.{Await, Future}
import scala.util.{Success, Try}


/**
 * Happy scenario test
 */

class JokeEtlProcessTest
  extends AnyFlatSpec
    with Matchers
    with BeforeAndAfterEach
    with FlywaySupport
    with H2DBConnProperties {

  override protected def beforeEach(): Unit = {
    super.beforeEach()
    cleanDB()
    migrateDB()
  }

  object JokeDAOH2 extends JokeDAO {
    override val conn = H2Conn.conn
    override val profile = H2Conn.profile
  }

  class JokesRestClientMock(httpResponse: HttpResponse) extends JokesRestClient {
    override def get(jokeRestParams: JokeRestParams, endpoint: String): Future[HttpResponse] = {
      Future.successful[HttpResponse](httpResponse)
    }
  }

  "A JokeEtlProcess" should "correctly return Success(Some(1)) after insertion of downloaded Joke to H2" in {
    val jokeRestParams = JokeRestParams(idRange = Some(Range.inclusive(0, 0)))

    val httpResponseMock = HttpResponse(StatusCodes.OK,
      entity = HttpEntity(
        """
          |{
          |    "error": false,
          |    "amount": 1,
          |    "jokes": [
          |        {
          |            "category": "Programming",
          |            "type": "single",
          |            "joke": "I've got a really good UDP joke to tell you but I don’t know if you'll get it.",
          |            "flags": {
          |                "nsfw": false,
          |                "religious": false,
          |                "political": false,
          |                "racist": false,
          |                "sexist": false,
          |                "explicit": false
          |            },
          |            "id": 0,
          |            "safe": true,
          |            "lang": "en"
          |        }
          |    ]
          |}
          |""".stripMargin
      ))

    val restClientMock = new JokesRestClientMock(httpResponseMock)

    val expectedEntitiesFromH2 = Seq(
      Joke(
        0,
        JokeCategory.withName("Programming"),
        JokeType.withName("single"),
        "I've got a really good UDP joke to tell you but I don’t know if you'll get it.",
        true,
        "en")
    )

    val expectedResult = Success(Some(1))

    val result: Try[Option[Int]] = JokeEtlProcess.extractTransformLoad(jokeRestParams, JokeDAOH2, restClientMock)

    assert(result === expectedResult)
    assert(Await.result(JokeDAOH2.get(), FiniteDuration(5, SECONDS)) === expectedEntitiesFromH2)
  }
}

