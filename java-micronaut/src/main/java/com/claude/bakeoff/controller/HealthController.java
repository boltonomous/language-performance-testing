package com.claude.bakeoff.controller;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

import java.util.Map;

@Controller
public class HealthController {
    
    @Get("/health")
    public HttpResponse<Map<String, Object>> health() {
        Map<String, Object> response = Map.of(
            "status", "OK",
            "service", "java-micronaut",
            "timestamp", System.currentTimeMillis()
        );
        
        return HttpResponse.ok(response)
                .header("Cache-Control", "public, max-age=60")
                .header("Surrogate-Control", "max-age=60");
    }
}