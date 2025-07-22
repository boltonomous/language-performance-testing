package com.claude.bakeoff.controller

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get

@Controller
open class HealthController {
    
    @Get("/health")
    open fun health(): HttpResponse<Map<String, Any>> {
        val response = mapOf<String, Any>(
            "status" to "OK",
            "service" to "kotlin-micronaut", 
            "timestamp" to System.currentTimeMillis()
        )
        
        return HttpResponse.ok(response)
            .header("Cache-Control", "public, max-age=60")
            .header("Surrogate-Control", "max-age=60")
    }
}