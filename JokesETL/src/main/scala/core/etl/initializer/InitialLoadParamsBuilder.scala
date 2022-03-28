package core.etl.initializer

import core.rest.JokeRestParams
import core.rest.config.RestConfig
import core.db.entities.enums.{JokeCategory, JokeType}

trait InitialLoadParamsBuilder {
  private[initializer] def initialLoadRestRequestsParams(categories: Seq[JokeCategory] = Nil,
                                                         jokeType: Option[JokeType] = None,
                                                         numberOfJokes: Int = RestConfig.initialLoadSize,
                                                         batchSize: Int = RestConfig.batchSize): Seq[JokeRestParams] = {

    val firstAndLastBatchIds =
      Range.inclusive(1, numberOfJokes)
        .grouped(batchSize)
        .map(batchIds => (batchIds.head, batchIds.last))

    firstAndLastBatchIds
      .map { case (firstId, lastId) => JokeRestParams(categories, jokeType, Some(Range.inclusive(firstId, lastId))) }
      .toSeq
  }
}

object InitialLoadParamsBuilder extends InitialLoadParamsBuilder
