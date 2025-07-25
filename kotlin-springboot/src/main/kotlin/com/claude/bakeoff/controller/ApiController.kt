package com.claude.bakeoff.controller

import com.claude.bakeoff.model.ComputeRequest
import com.claude.bakeoff.model.ComputeResponse
import com.claude.bakeoff.model.HealthResponse
import com.claude.bakeoff.model.UsersResponse
import com.claude.bakeoff.repository.UserRepository
import com.claude.bakeoff.service.MathService
import org.springframework.http.CacheControl
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import jakarta.validation.Valid
import java.util.concurrent.TimeUnit

@RestController
class ApiController(
    private val mathService: MathService,
    private val userRepository: UserRepository
) {
    
    @GetMapping("/health")
    fun health(): ResponseEntity<HealthResponse> {
        val response = HealthResponse(
            status = "OK",
            service = "kotlin-springboot",
            timestamp = System.currentTimeMillis()
        )
        
        return ResponseEntity.ok()
            .cacheControl(CacheControl.maxAge(60, TimeUnit.SECONDS))
            .header("Surrogate-Control", "max-age=60")
            .body(response)
    }
    
    @PostMapping("/api/compute")
    fun compute(@Valid @RequestBody request: ComputeRequest): ResponseEntity<ComputeResponse> {
        val startTime = System.currentTimeMillis()
        
        // Validate request
        require(request.numbers.isNotEmpty()) { "Numbers list cannot be empty" }
        require(request.operation.isNotBlank()) { "Operation cannot be blank" }
        
        val result = mathService.compute(request.numbers, request.operation)
        
        val processingTime = System.currentTimeMillis() - startTime
        
        val response = ComputeResponse(
            result = result,
            operation = request.operation,
            inputSize = request.numbers.size,
            processingTimeMs = processingTime
        )
        
        return ResponseEntity.ok(response)
    }
    
    @GetMapping("/api/users")
    fun getUsers(@RequestParam(required = false) department: String?): ResponseEntity<UsersResponse> {
        val startTime = System.currentTimeMillis()
        
        val users = if (department.isNullOrEmpty()) {
            userRepository.findAll()
        } else {
            userRepository.findByDepartment(department)
        }
        
        val processingTime = System.currentTimeMillis() - startTime
        
        val response = UsersResponse(
            users = users,
            count = users.size,
            totalUsers = userRepository.count(),
            processingTimeMs = processingTime
        )
        
        return ResponseEntity.ok()
            .cacheControl(CacheControl.maxAge(300, TimeUnit.SECONDS))
            .header("Surrogate-Control", "max-age=300")
            .body(response)
    }
}