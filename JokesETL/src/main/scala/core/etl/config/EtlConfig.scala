package core.etl.config

import core.utils.ConfigSupport

import scala.concurrent.duration.{FiniteDuration, MILLISECONDS}

object EtlConfig extends ConfigSupport {
  val parallelism: Int = config.getInt("jokes.etl.rest.parallelism")
  val processingTimeout: FiniteDuration =
    FiniteDuration(config.getLong("jokes.etl.processing.timeout.millis"), MILLISECONDS)
}
