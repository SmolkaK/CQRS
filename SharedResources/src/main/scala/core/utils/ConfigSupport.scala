package core.utils

import com.typesafe.config.{Config, ConfigFactory}

trait ConfigSupport {
  protected val config: Config = ConfigFactory.load()
}
