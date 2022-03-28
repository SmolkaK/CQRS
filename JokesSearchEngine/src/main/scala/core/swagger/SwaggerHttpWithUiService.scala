package core.swagger

import akka.http.javadsl.server.Rejections
import akka.http.scaladsl.model.StatusCodes.PermanentRedirect
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Route
import akka.util.ByteString
import com.github.swagger.akka.SwaggerHttpService
import core.swagger.SwaggerHttpWithUiService.swaggerUi
import org.webjars.{MultipleMatchesException, NotFoundException, WebJarAssetLocator}

import scala.concurrent.Future
import scala.io.Source
import scala.util.{Failure, Success, Try}

trait SwaggerHttpWithUiService extends SwaggerHttpService {
  val webJarAssetLocator = new WebJarAssetLocator()

  def webJars(webJarName: String, indexContent: Future[HttpEntity.Strict]): Route = {
    concat(
      pathEnd {
        redirect(s"/$apiDocsPath/", PermanentRedirect)
      },
      pathSingleSlash {
        complete(indexContent)
      },
      extractUnmatchedPath { path =>
        Try(webJarAssetLocator.getFullPath(webJarName, path.toString)) match {
          case Success(fullPath) =>
            getFromResource(fullPath)
          case Failure(_: NotFoundException) =>
            reject
          case Failure(e: MultipleMatchesException) =>
            reject(Rejections.validationRejection(e.getMessage))
          case Failure(e) => failWith(e)
        }
      })
  }

  def swaggerIndex(classLoader: ClassLoader = _defaultClassLoader): Future[HttpEntity.Strict] = {
    Future.fromTry(Try {
      val fullPath = webJarAssetLocator.getFullPath(swaggerUi, "index.html")
      val url = classLoader.getResource(fullPath)
      val content = Source.fromURL(url).mkString
        .replaceFirst("https://petstore.swagger.io/v2/swagger.json", s"/$apiDocsPath/swagger.json")
      HttpEntity.Strict(ContentTypes.`text/html(UTF-8)`, ByteString(content))
    })
  }

  val swaggerUiRoute: Route = {
    pathPrefix(apiDocsPath) {
      webJars(swaggerUi, swaggerIndex())
    }
  }

  override val routes: Route = super.routes ~ swaggerUiRoute
}

object SwaggerHttpWithUiService {
  val swaggerUi = "swagger-ui"
}
