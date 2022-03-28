package core.swagger.config

import core.utils.ConfigSupport

object SwaggerConfig extends ConfigSupport {
  val host: String = config.getString("rest.swagger.host")
  val port: Int = config.getInt("rest.swagger.port")
}
