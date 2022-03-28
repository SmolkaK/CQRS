package core.db.connections

import slick.jdbc.JdbcProfile

trait DBConn {
  protected val conn: JdbcProfile#API#Database
  protected val profile: JdbcProfile
}
