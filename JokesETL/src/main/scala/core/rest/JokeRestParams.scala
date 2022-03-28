package core.rest

import core.db.entities.enums.{JokeCategory, JokeType}

case class JokeRestParams(categories: Seq[JokeCategory] = Nil,
                          jokeType: Option[JokeType] = Some(JokeType.Single),
                          idRange: Option[Range] = None) {
  override def toString: String = s"JokeRestParams(categories=$categories, jokeType=$jokeType, idRange=$idRange)"
}

