package core.etl

import core.db.dao.JokeDAO
import core.db.entities.Joke
import core.etl.config.EtlConfig
import core.rest.JokeRestParams
import core.rest.clients.JokesRestClient
import core.rest.config.RestConfig.restEntityStreamCollectionTimeout
import core.rest.parsers.json.JokeJsonReadSupport
import core.utils.ActorUtils
import play.api.libs.json.Json

import scala.concurrent.Await
import scala.util.Try

object JokeEtlProcess extends ActorUtils with JokeJsonReadSupport {

  private[etl] def extractTransformLoad(jokeRestParams: JokeRestParams,
                                        jokeDAO: JokeDAO = JokeDAO,
                                        jokesRestClient: JokesRestClient = JokesRestClient): Try[Option[Int]] = {
    Try(
      jokesRestClient.get(jokeRestParams)
        .flatMap(response => response.entity.toStrict(restEntityStreamCollectionTimeout))
        .map(body => Json.parse(body.data.utf8String).as[Seq[Joke]])
        .flatMap(jokeDAO.insert)
    ).flatMap(etlResult => Try(Await.result(etlResult, EtlConfig.processingTimeout)))
  }
}
