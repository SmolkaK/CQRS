package core.db.connections.properties

trait DBConnProperties {
  val jdbcUrl: String
  val user: String
  val password: Option[String]
}
