package core.rest.clients

import core.db.entities.enums.{JokeCategory, JokeType}

private[clients] object JokesRestUriBuilder {
  def uri(endpoint: String,
          categories: Seq[JokeCategory],
          jokeType: Option[JokeType],
          idRange: Option[Range]): String = {

    val categoriesAsParams: String =
      "/" + (if (categories.nonEmpty) categories.map(_.toString).mkString(",") else JokeCategory.Any.toString)
    val idRangeAsParams = idRange.map(range => s"idRange=${range.start}-${range.end}&amount=${range.end-range.start+1}")
    val jokeTypeAsParam = jokeType.map(jokeTypeExtracted => s"type=$jokeTypeExtracted")

    Seq(idRangeAsParams, jokeTypeAsParam).flatten.mkString("&") match {
      case params if params.isEmpty => s"$endpoint$categoriesAsParams"
      case params => s"$endpoint$categoriesAsParams?$params"
    }
  }
}
