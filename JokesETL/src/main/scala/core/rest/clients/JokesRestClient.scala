package core.rest.clients

import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpMethods, HttpRequest, HttpResponse}
import core.rest.JokeRestParams
import core.rest.config.RestConfig
import core.utils.ActorUtils

import scala.concurrent.Future

trait JokesRestClient extends ActorUtils {

  def get(jokeRestParams: JokeRestParams, endpoint: String = RestConfig.endpoint): Future[HttpResponse] = {

    val uri = JokesRestUriBuilder.uri(
      endpoint,
      jokeRestParams.categories,
      jokeRestParams.jokeType,
      jokeRestParams.idRange
    )

    val httpRequest = HttpRequest(HttpMethods.GET, uri = uri)

    Http()(actorSystem).singleRequest(httpRequest)
  }

}

object JokesRestClient extends JokesRestClient