package core.db.connections.properties

import core.db.config.H2Config

trait H2DBConnProperties extends DBConnProperties {
  val jdbcUrl: String = H2Config.jdbcUrl
  val user: String = ""
  val password: Option[String] = None
}
