package core.db.connections

import core.db.connections.properties.PostgresDBConnProperties
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.JdbcProfile

trait PostgresConn extends DBConn{
  protected val conn: Database = PostgresConn.conn
  protected val profile: JdbcProfile = PostgresConn.profile
}

object PostgresConn extends PostgresDBConnProperties {
  val conn: Database = Database.forURL(jdbcUrl, user, password.orNull)
  val profile: JdbcProfile = slick.jdbc.PostgresProfile
}