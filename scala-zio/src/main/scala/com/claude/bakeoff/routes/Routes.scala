package com.claude.bakeoff.routes

import com.claude.bakeoff.model.*
import com.claude.bakeoff.repository.UserRepository
import com.claude.bakeoff.service.MathService
import zio.*
import zio.http.*
import zio.json.*

object Routes:
  
  val app = 
    zio.http.Routes(
      Method.GET / "health" -> handler { (_: Request) =>
        val healthResponse = HealthResponse(
          status = "OK",
          service = "scala-zio",
          timestamp = java.lang.System.currentTimeMillis()
        )
        ZIO.succeed(Response.json(healthResponse.toJson))
      },
      
      Method.POST / "api" / "compute" -> handler { (req: Request) =>
        for
          startTime <- Clock.currentTime(java.util.concurrent.TimeUnit.MILLISECONDS)
          body <- req.body.asString
          computeRequest <- ZIO.fromEither(body.fromJson[ComputeRequest])
            .mapError(_ => Response.json("""{"error": "Invalid JSON"}""").status(Status.BadRequest))
          _ <- ZIO.when(computeRequest.numbers.isEmpty)(
            ZIO.fail(Response.json("""{"error": "Numbers list cannot be empty"}""").status(Status.BadRequest))
          )
          _ <- ZIO.when(computeRequest.operation.trim.isEmpty)(
            ZIO.fail(Response.json("""{"error": "Operation cannot be blank"}""").status(Status.BadRequest))
          )
          result <- ZIO.serviceWithZIO[MathService](_.compute(computeRequest.numbers, computeRequest.operation))
            .mapError(error => Response.json(s"""{"error": "$error"}""").status(Status.BadRequest))
          endTime <- Clock.currentTime(java.util.concurrent.TimeUnit.MILLISECONDS)
          processingTime = endTime - startTime
          response = ComputeResponse(
            result = result,
            operation = computeRequest.operation,
            inputSize = computeRequest.numbers.size,
            processingTimeMs = processingTime
          )
        yield Response.json(response.toJson)
      },
      
      Method.GET / "api" / "users" -> handler { (req: Request) =>
        for
          startTime <- Clock.currentTime(java.util.concurrent.TimeUnit.MILLISECONDS)
          department = req.url.queryParams.getAll("department").headOption.getOrElse("")
          users <- if (department.isEmpty) 
            ZIO.serviceWithZIO[UserRepository](_.findAll())
          else 
            ZIO.serviceWithZIO[UserRepository](_.findByDepartment(department))
          totalUsers <- ZIO.serviceWithZIO[UserRepository](_.count())
          endTime <- Clock.currentTime(java.util.concurrent.TimeUnit.MILLISECONDS)
          processingTime = endTime - startTime
          response = UsersResponse(
            users = users,
            count = users.size,
            totalUsers = totalUsers,
            processingTimeMs = processingTime
          )
        yield Response.json(response.toJson)
      }
    ).sandbox.mapError {
      case response: Response => response
      case _ => Response.json("""{"error": "Internal server error"}""").status(Status.InternalServerError)
    }