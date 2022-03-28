package core.etl

import akka.actor.{Actor, ActorRef, Props}
import akka.routing.SmallestMailboxPool
import core.etl.config.EtlConfig
import core.rest.JokeRestParams
import core.rest.parsers.json.JokeJsonReadSupport
import core.utils.{ActorUtils, Logger}
import play.api.libs.json.JsResultException

import scala.util.{Failure, Success}

/**
 * INFO:
 * Można było tutaj rozdzielić odczyt z rest-ów od zapisu do bazy, tak by móc je skalować osobno,
 * jednak wydaje mi się to nadmiarowe, szczególnie że mamy do dyspozycji connectionPoola bazy danych.
 * W zamian dostajemy przejrzystość i mniej klas.
 *
 * Await jest zastosowany w celu przekierowania zrównloleglenia z future-ów bezpośrednio na aktorów, gdzie łatwiej jest nim zarządzać.
 */
class JokeEtlActor
  extends Actor
    with JokeJsonReadSupport
    with ActorUtils
    with Logger {

  override def receive: Receive = {
    case jokeRestParams: JokeRestParams =>
      JokeEtlProcess.extractTransformLoad(jokeRestParams) match {
        case Success(Some(numberOfInserted)) =>
          log.info(s"Jokes ETL successfully processed $numberOfInserted jokes for params: $jokeRestParams")
        case Success(None) =>
          log.warn(s"Jokes ETL inserted 0 jokes for params: $jokeRestParams")
        case Failure(ex: JsResultException) if ex.getMessage.contains(""""causedBy":["No jokes were found that match your provided filter(s)."]""") =>
          log.info(s"No jokes were found that match your provided filter(s): $jokeRestParams")
        case Failure(ex) =>
          log.error(s"Jokes ETL process failed for params: $jokeRestParams", ex)
      }
  }
}

object JokeEtlActor {
  val jokesRestETLActorRef: ActorRef =
    ActorUtils.actorSystem.actorOf(SmallestMailboxPool(EtlConfig.parallelism).props(Props(new JokeEtlActor())))
}
