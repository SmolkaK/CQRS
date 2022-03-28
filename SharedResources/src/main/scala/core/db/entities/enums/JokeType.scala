package core.db.entities.enums

import akka.http.scaladsl.unmarshalling.Unmarshaller
import enumeratum._

import scala.collection.immutable

sealed trait JokeType extends EnumEntry

object JokeType extends Enum[JokeType] {
  override def values: immutable.IndexedSeq[JokeType] = findValues

  implicit val jokeTypeFromStringUnmarshaller: Unmarshaller[String, JokeType] =
    Unmarshaller.strict[String, JokeType] { string => JokeType.withName(string) }

  case object Single extends JokeType {
    override def toString: String = "single"
  }
  case object TwoPart extends JokeType {
    override def toString: String = "twopart"
  }
}