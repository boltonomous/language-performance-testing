package com.claude.bakeoff.routing

import com.claude.bakeoff.model.ComputeRequest
import com.claude.bakeoff.model.ComputeResponse
import com.claude.bakeoff.model.HealthResponse
import com.claude.bakeoff.model.UsersResponse
import com.claude.bakeoff.repository.UserRepository
import com.claude.bakeoff.service.MathService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting(userRepository: UserRepository) {
    val mathService = MathService()
    
    routing {
        get("/health") {
            call.response.cacheControl(CacheControl.MaxAge(maxAgeSeconds = 60))
            call.response.header("Surrogate-Control", "max-age=60")
            
            call.respond(
                HealthResponse(
                    status = "OK",
                    service = "kotlin-ktor",
                    timestamp = System.currentTimeMillis()
                )
            )
        }
        
        post("/api/compute") {
            val startTime = System.currentTimeMillis()
            
            val request = call.receive<ComputeRequest>()
            
            // Validate request
            require(request.numbers.isNotEmpty()) { "Numbers list cannot be empty" }
            require(request.operation.isNotBlank()) { "Operation cannot be blank" }
            
            val result = mathService.compute(request.numbers, request.operation)
            
            val processingTime = System.currentTimeMillis() - startTime
            
            call.respond(
                ComputeResponse(
                    result = result,
                    operation = request.operation,
                    inputSize = request.numbers.size,
                    processingTimeMs = processingTime
                )
            )
        }
        
        get("/api/users") {
            call.response.cacheControl(CacheControl.MaxAge(maxAgeSeconds = 300))
            call.response.header("Surrogate-Control", "max-age=300")
            
            val startTime = System.currentTimeMillis()
            val department = call.request.queryParameters["department"] ?: ""
            
            val users = if (department.isEmpty()) {
                userRepository.findAll()
            } else {
                userRepository.findByDepartment(department)
            }
            
            val processingTime = System.currentTimeMillis() - startTime
            
            call.respond(
                UsersResponse(
                    users = users,
                    count = users.size,
                    totalUsers = userRepository.count(),
                    processingTimeMs = processingTime
                )
            )
        }
    }
}