package core.rest.config

import core.utils.ConfigSupport

import scala.concurrent.duration.{FiniteDuration, MILLISECONDS}

object RestConfig extends ConfigSupport {
  val endpoint: String = config.getString("jokes.etl.rest.endpoint")
  val initialLoadSize: Int = config.getInt("jokes.etl.rest.initialLoad.size")
  val batchSize: Int = config.getInt("jokes.etl.rest.batchSize")
  val restEntityStreamCollectionTimeout: FiniteDuration =
    FiniteDuration(config.getLong("jokes.etl.rest.entityStreamCollection.timeout.millis"), MILLISECONDS)
}
