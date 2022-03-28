package core.db.config

import core.utils.ConfigSupport

object PostgresConfig extends ConfigSupport {
  val host: String = config.getString("database.postgres.host")
  val user: String = config.getString("database.postgres.user")
  val password: String = config.getString("database.postgres.password")
  val jdbcUrl: String = s"jdbc:postgresql://$host/$user"
}