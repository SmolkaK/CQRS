package core.parsers.json

import core.db.entities.Joke
import play.api.libs.json.{JsValue, Json, Writes}

trait JokeJsonWriteSupport {
  implicit val jokesWrites: Writes[Joke] = new JokeWrites()

  private class JokeWrites() extends Writes[Joke] {
    override def writes(joke: Joke): JsValue = {
      Json.obj(
        "id" -> joke.id,
        "category" -> joke.category.toString,
        "type" -> joke.`type`.toString,
        "content" -> joke.content,
        "is_safe" -> joke.is_safe,
        "lang" -> joke.lang
      )
    }
  }
}
