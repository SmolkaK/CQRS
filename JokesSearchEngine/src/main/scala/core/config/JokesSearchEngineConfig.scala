package core.config

import core.utils.ConfigSupport

object JokesSearchEngineConfig extends ConfigSupport {
  val host: String = config.getString("rest.server.host")
  val port: Int = config.getInt("rest.server.port")
}