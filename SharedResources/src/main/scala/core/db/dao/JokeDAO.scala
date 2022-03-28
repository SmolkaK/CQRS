package core.db.dao

import core.db.connections.PostgresConn
import core.db.entities.Joke
import core.db.entities.enums.{JokeCategory, JokeType}
import core.db.tables.JokeTable
import core.utils.ActorUtils
import slick.ast.BaseTypedType
import slick.jdbc.JdbcType

import scala.concurrent.Future

/**
 * INFO:
 * Podzielenie DAO na DAOWriter/DAOReader w przypadku stosowania CQRS w <u>całej</u> aplikacji, wydaje się mieć szereg zalet.
 * (np. ustandaryzowanie odpowiedniego typu sesji, czy jasne rozdzielenie odpowiedzialności klas przez co nawet osoba
 * nowa w projekcie, nie zastosuje read w klasie write). Pytanie co z sytuacjami gdy mimo najlepszych chęci musimy połączyć
 * read z write.
 * Ten temat doprowadził mnie do wniosku, że podział Read/Write w CQRS powinien być podziałem na poziomie biznesowym,
 * a nie fizycznym. To że np. nasz mikroserwis ETL oprócz zapisywania jeszcze coś odczytuje z bazy,
 * nie ma tak naprawdę znaczenia dopóki nie wpływa to znacząco na szybkość zapisu (np. poprzez zmianę typu sesji).
 * Ważne jest to, żeby najcięższy strumień danych do zapisu był odseparowany od najcięższego strumienia danych odczytywanych,
 * (mikroserwisy, osobne tabele, bazy itp.), i można było je swobodnie niezależnie skalować wydajnościowo.
 *
 * Z chęcią dowiem się jak Wy rozumiecie ten problem ;)
 */

trait JokeDAO extends PostgresConn with ActorUtils {

  import profile.api._

  private val table: profile.api.TableQuery[JokeTable] = TableQuery[JokeTable]

  private implicit lazy val jokeTypeColumnType: JdbcType[JokeType] with BaseTypedType[JokeType] =
    MappedColumnType.base[JokeType, String](
      { c => c.toString },
      { s => JokeType.withName(s) }
    )

  private implicit lazy val jokeCategoryColumnType: JdbcType[JokeCategory] with BaseTypedType[JokeCategory] =
    MappedColumnType.base[JokeCategory, String](
      { c => c.toString },
      { s => JokeCategory.withName(s) }
    )

  def get(): Future[Seq[Joke]] = {
    conn.run(table.result)
  }

  def get(id: Option[Long],
          `type`: Option[JokeType],
          category: Option[JokeCategory],
          is_safe: Option[Boolean],
          lang: Option[String],
         ): Future[Seq[Joke]] = {
    conn.run(
      table
        .filterOpt(id)((e, v) => e.id === v)
        .filterOpt(`type`)((e, v) => e.`type` === v)
        .filterOpt(category)((e, v) => e.category === v)
        .filterOpt(is_safe)((e, v) => e.is_safe === v)
        .filterOpt(lang)((e, v) => e.lang === v)
        .result
    )
  }

  def insert(jokes: Seq[Joke]): Future[Option[Int]] = {
    conn.run(table ++= jokes)
  }

}

object JokeDAO extends JokeDAO