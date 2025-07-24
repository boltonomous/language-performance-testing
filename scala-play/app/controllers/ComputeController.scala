package controllers

import models.{ComputeRequest, ComputeResponse, UsersResponse}
import play.api.libs.json.Json
import play.api.mvc._
import repositories.UserRepository
import services.MathService

import javax.inject._
import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success, Try}

@Singleton
class ComputeController @Inject()(
  val controllerComponents: ControllerComponents,
  mathService: MathService,
  userRepository: UserRepository
)(implicit ec: ExecutionContext) extends BaseController {

  def compute(): Action[ComputeRequest] = Action(parse.json[ComputeRequest]) { implicit request: Request[ComputeRequest] =>
    val startTime = System.currentTimeMillis()
    val computeRequest = request.body
    
    // Validate request
    if (computeRequest.numbers.isEmpty) {
      BadRequest(Json.obj("error" -> "Numbers list cannot be empty"))
    } else if (computeRequest.operation.trim.isEmpty) {
      BadRequest(Json.obj("error" -> "Operation cannot be blank"))
    } else {
      Try {
        val result = mathService.compute(computeRequest.numbers, computeRequest.operation)
        val processingTime = System.currentTimeMillis() - startTime
        
        ComputeResponse(
          result = result,
          operation = computeRequest.operation,
          inputSize = computeRequest.numbers.size,
          processingTimeMs = processingTime
        )
      } match {
        case Success(response) => Ok(Json.toJson(response))
        case Failure(exception) => BadRequest(Json.obj("error" -> exception.getMessage))
      }
    }
  }

  def getUsers(department: Option[String]): Action[AnyContent] = Action.async { implicit request =>
    val startTime = System.currentTimeMillis()
    
    val usersFuture = department.filter(_.nonEmpty) match {
      case Some(dept) => userRepository.findByDepartment(dept)
      case None => userRepository.findAll()
    }
    
    for {
      users <- usersFuture
      totalUsers <- userRepository.count()
    } yield {
      val processingTime = System.currentTimeMillis() - startTime
      
      val response = UsersResponse(
        users = users,
        count = users.size,
        totalUsers = totalUsers,
        processingTimeMs = processingTime
      )
      
      Ok(Json.toJson(response))
        .withHeaders(
          "Cache-Control" -> "public, max-age=300",
          "Surrogate-Control" -> "max-age=300"
        )
    }
  }
}