package core.routes

import akka.http.scaladsl.model.ContentTypes.`application/json`
import akka.http.scaladsl.model.StatusCodes.OK
import akka.http.scaladsl.model.{HttpEntity, HttpResponse}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import core.db.dao.JokeDAO
import core.db.entities.Joke
import core.db.entities.enums.JokeCategory.jokeCategoryFromStringUnmarshaller
import core.db.entities.enums.JokeType.jokeTypeFromStringUnmarshaller
import core.db.entities.enums.{JokeCategory, JokeType}
import core.parsers.json.JokeJsonWriteSupport
import core.utils.Logger
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.media.{Content, Schema}
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.{Operation, Parameter}
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.{GET, Path, Produces}
import play.api.libs.json.Json


@Path("/jokes")
object JokeRoutes extends JokeJsonWriteSupport with Logger {

  def routes: Route = getJokes

  @GET
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Operation(summary = "Returns jokes", description = "Returns jokes that fits specified parameters. All jokes will be returned if no parameters are provided.",
    parameters = Array(
      new Parameter(name = "id", in = ParameterIn.QUERY, description = "id", required = false),
      new Parameter(name = "type", in = ParameterIn.QUERY, description = "type", required = false),
      new Parameter(name = "category", in = ParameterIn.QUERY, description = "category", required = false),
      new Parameter(name = "is_safe", in = ParameterIn.QUERY, description = "is_safe", required = false),
      new Parameter(name = "lang", in = ParameterIn.QUERY, description = "lang", required = false)
    ),
    responses = Array(
      new ApiResponse(responseCode = "200", description = "Jokes that fits params",
        content = Array(new Content(schema = new Schema(implementation = classOf[Joke])))),
      new ApiResponse(responseCode = "400", description = "Bad Request"),
      new ApiResponse(responseCode = "500", description = "Internal server error"))
  )
  def getJokes: Route =
    pathPrefix("jokes") {
      pathEnd {
        extractRequest { request =>
          log.info(s"Received REST request: $request")

          parameters(
            "id".as[Long].optional,
            "type".as[JokeType].optional,
            "category".as[JokeCategory].optional,
            "is_safe".as[Boolean].optional,
            "lang".optional
          ) { (id, `type`, category, is_safe, lang) =>
            get {
              onSuccess(JokeDAO.get(id, `type`, category, is_safe, lang)) { jokes =>
                complete(HttpResponse(status = OK, entity = HttpEntity(`application/json`, Json.prettyPrint(Json.toJson(jokes)))))
              }
            }
          }
        }
      }
    }

}
