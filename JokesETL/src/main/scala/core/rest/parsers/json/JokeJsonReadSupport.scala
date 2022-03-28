package core.rest.parsers.json

import core.db.entities.Joke
import core.db.entities.enums.{JokeCategory, JokeType}
import core.utils.Logger
import play.api.libs.json._

import scala.util.{Failure, Success, Try}


trait JokeJsonReadSupport extends Logger {

  implicit val jokesReads: Reads[Seq[Joke]] = new JokesReads()

  private class JokesReads() extends Reads[Seq[Joke]] {
    implicit val jokeReads: Reads[Joke] = new JokeReads()

    def reads(js: JsValue): JsResult[Seq[Joke]] = {
      Try(
        if (amount(js).isDefined) (js \ "jokes").as[JsArray].value.map(jsJoke => jsJoke.as[Joke]) else Seq(js.as[Joke])
      ) match {
        case Success(jokes) =>
          log.debug(s"${jokes.size}. jokes parsed successfully. JsValue: $js")
          JsSuccess(jokes)
        case Failure(ex) =>
          log.error(s"Couldn't create Joke from json. JsValue: $js", ex)
          JsError(s"Couldn't create Joke from json. JsValue: $js")
      }
    }

    private def amount(js: JsValue) = {
      (js \ "amount").toOption
    }
  }

  private class JokeReads() extends Reads[Joke] {
    def reads(js: JsValue): JsResult[Joke] = {
      Try(
        Joke(
          id = (js \ "id").as[Int],
          category = JokeCategory.withName((js \ "category").as[String]),
          `type` = JokeType.withName((js \ "type").as[String]),
          content = content(js),
          is_safe = (js \ "safe").as[Boolean],
          lang = (js \ "lang").as[String]
        )
      ) match {
        case Success(joke) =>
          log.debug(s"Joke parsed successfully. JsValue: $js")
          JsSuccess(joke)
        case Failure(ex) =>
          log.error(s"Couldn't create Joke from json. JsValue: $js", ex)
          JsError(s"Couldn't create Joke from json. JsValue: $js")
      }
    }

    private def content(js: JsValue): String = {
      if ((js \ "type").as[String] == "single") {
        (js \ "joke").as[String]
      } else {
        (js \ "setup").as[String] + " >>>" + (js \ "delivery").as[String]
      }
    }
  }
}