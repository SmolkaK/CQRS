package core.db.entities.enums

import akka.http.scaladsl.unmarshalling.Unmarshaller
import enumeratum._

import scala.collection.immutable

sealed trait JokeCategory extends EnumEntry

object JokeCategory extends Enum[JokeCategory] {
  override def values: immutable.IndexedSeq[JokeCategory] = findValues

  implicit val jokeCategoryFromStringUnmarshaller: Unmarshaller[String, JokeCategory] =
    Unmarshaller.strict[String, JokeCategory] { string => JokeCategory.withName(string) }

  case object Programming extends JokeCategory
  case object Misc extends JokeCategory
  case object Dark extends JokeCategory
  case object Pun extends JokeCategory
  case object Spooky extends JokeCategory
  case object Christmas extends JokeCategory
  case object Any extends JokeCategory
}