package core.db.config

import core.utils.ConfigSupport
import scala.collection.JavaConverters._

object H2Config extends ConfigSupport {
  val name: String = config.getString("database.h2.name")
  val dir: String = config.getString("database.h2.dir")
  val properties: Seq[String] = config.getStringList("database.h2.properties").asScala
  val jdbcUrl: String = (s"jdbc:h2:$dir" +: properties).mkString(";")
}