package core

import akka.http.scaladsl.Http
import akka.http.scaladsl.server.RouteConcatenation
import core.config.JokesSearchEngineConfig
import core.db.connections.properties.PostgresDBConnProperties
import core.db.flyway.FlywaySupport
import core.routes.JokeRoutes
import core.swagger.SwaggerService
import core.utils.{ActorUtils, Logger}

import scala.util.{Failure, Success}

object JokesSearchEngineMain
  extends App
    with FlywaySupport
    with PostgresDBConnProperties
    with ActorUtils
    with RouteConcatenation
    with Logger {

  migrateDB()

  Http().newServerAt(JokesSearchEngineConfig.host, JokesSearchEngineConfig.port).bind(JokeRoutes.routes ~ SwaggerService.routes)
    .onComplete {
      case Success(binding) =>
        log.info("Server is up and running://{}:{}/", binding.localAddress.getHostString, binding.localAddress.getPort)
      case Failure(ex) =>
        log.error("Failed to bind HTTP endpoint, terminating system", ex)
        actorSystem.terminate()
    }
}