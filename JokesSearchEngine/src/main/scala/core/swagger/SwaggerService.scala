package core.swagger

import com.github.swagger.akka.model.Info
import core.routes.JokeRoutes
import core.swagger.config.SwaggerConfig

object SwaggerService extends SwaggerHttpWithUiService {
  override val apiClasses = Set(JokeRoutes.getClass)
  override val host = s"${SwaggerConfig.host}:${SwaggerConfig.port}"
  override val info: Info = Info(version = "1.0")
  override val unwantedDefinitions = Seq("Function1", "Function1RequestContextFutureRouteResult")
}