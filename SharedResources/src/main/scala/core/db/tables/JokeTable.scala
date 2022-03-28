package core.db.tables

import core.db.entities.Joke
import core.db.entities.enums.{JokeCategory, JokeType}
import slick.ast.BaseTypedType
import slick.jdbc.H2Profile.api._
import slick.jdbc.JdbcType

class JokeTable(tag: Tag) extends Table[Joke](tag, None, "joke") {
  override def * = (id, category, `type`,  content,  is_safe,  lang) <> (Joke.tupled, Joke.unapply)

  implicit val jokeTypeMapper: JdbcType[JokeType] with BaseTypedType[JokeType] = MappedColumnType.base[JokeType, String](
    e => e.toString,
    s => JokeType.withName(s)
  )

  implicit val jokeCategoryMapper: JdbcType[JokeCategory] with BaseTypedType[JokeCategory] = MappedColumnType.base[JokeCategory, String](
    e => e.toString,
    s => JokeCategory.withName(s)
  )

  val id : Rep[Long] = column[Long]("id", O.PrimaryKey)
  val category: Rep[JokeCategory] = column[JokeCategory]("category")
  val `type` : Rep[JokeType] = column[JokeType]("type")
  val content : Rep[String] = column[String]("content")
  val is_safe : Rep[Boolean] = column[Boolean]("is_safe")
  val lang : Rep[String] = column[String]("lang")
}
