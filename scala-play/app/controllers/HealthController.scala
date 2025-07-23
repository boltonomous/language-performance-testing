package controllers

import models.HealthResponse
import play.api.libs.json.Json
import play.api.mvc._

import javax.inject._

@Singleton
class HealthController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  def health(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    val response = HealthResponse(
      status = "OK",
      service = "scala-play",
      timestamp = System.currentTimeMillis()
    )
    
    Ok(Json.toJson(response))
      .withHeaders(
        "Cache-Control" -> "max-age=60",
        "Surrogate-Control" -> "max-age=60"
      )
  }
}