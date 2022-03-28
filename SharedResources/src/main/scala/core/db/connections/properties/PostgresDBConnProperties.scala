package core.db.connections.properties

import core.db.config.PostgresConfig

trait PostgresDBConnProperties extends DBConnProperties {
  val jdbcUrl: String = PostgresConfig.jdbcUrl
  val user: String = PostgresConfig.user
  val password: Option[String] = Option(PostgresConfig.password).filter(_.trim.nonEmpty)
}