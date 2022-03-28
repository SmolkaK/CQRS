package core.db.connections

import core.db.connections.properties.H2DBConnProperties
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.JdbcProfile

trait H2Conn extends DBConn{
  protected val conn: Database = H2Conn.conn
  protected val profile: JdbcProfile = H2Conn.profile
}

object H2Conn extends H2DBConnProperties{
  val conn: Database = Database.forURL(jdbcUrl, user, password.orNull)
  val profile: JdbcProfile = slick.jdbc.H2Profile
}