package com.claude.bakeoff.controller

import com.claude.bakeoff.model.ComputeRequest
import com.claude.bakeoff.model.ComputeResponse
import com.claude.bakeoff.model.User
import com.claude.bakeoff.repository.UserRepository
import com.claude.bakeoff.service.MathService
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.QueryValue
import jakarta.validation.Valid

@Controller("/api")
open class ComputeController(
    private val mathService: MathService,
    private val userRepository: UserRepository
) {
    
    @Post("/compute")
    open fun compute(@Valid @Body request: ComputeRequest): ComputeResponse {
        val startTime = System.currentTimeMillis()
        
        val result = mathService.compute(request.numbers, request.operation)
        
        val processingTime = System.currentTimeMillis() - startTime
        
        return ComputeResponse(
            result = result,
            operation = request.operation,
            inputSize = request.numbers.size,
            processingTimeMs = processingTime
        )
    }
    
    @Get("/users")
    open fun getUsers(@QueryValue(defaultValue = "") department: String): HttpResponse<Map<String, Any>> {
        val startTime = System.currentTimeMillis()
        
        val users = if (department.isEmpty()) {
            userRepository.findAll()
        } else {
            userRepository.findByDepartment(department)
        }
        
        val totalUsers = userRepository.count()
        val processingTime = System.currentTimeMillis() - startTime
        
        val response = mapOf<String, Any>(
            "users" to users,
            "count" to users.size,
            "totalUsers" to totalUsers,
            "processingTimeMs" to processingTime
        )
        
        return HttpResponse.ok(response)
            .header("Cache-Control", "public, max-age=300")
            .header("Surrogate-Control", "max-age=300")
    }
}