package com.claude.bakeoff.controller;

import com.claude.bakeoff.model.ComputeRequest;
import com.claude.bakeoff.model.ComputeResponse;
import com.claude.bakeoff.model.User;
import com.claude.bakeoff.repository.UserRepository;
import com.claude.bakeoff.service.MathService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.QueryValue;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;

@Controller("/api")
public class ComputeController {
    
    private final MathService mathService;
    private final UserRepository userRepository;
    
    public ComputeController(MathService mathService, UserRepository userRepository) {
        this.mathService = mathService;
        this.userRepository = userRepository;
    }
    
    @Post("/compute")
    public ComputeResponse compute(@Valid @Body ComputeRequest request) {
        long startTime = System.currentTimeMillis();
        
        double result = mathService.compute(request.getNumbers(), request.getOperation());
        
        long processingTime = System.currentTimeMillis() - startTime;
        
        return new ComputeResponse(
            result,
            request.getOperation(),
            request.getNumbers().size(),
            processingTime
        );
    }
    
    @Get("/users")
    public HttpResponse<Map<String, Object>> getUsers(@QueryValue(defaultValue = "") String department) {
        long startTime = System.currentTimeMillis();
        
        List<User> users;
        if (department.isEmpty()) {
            users = userRepository.findAll();
        } else {
            users = userRepository.findByDepartment(department);
        }
        
        long processingTime = System.currentTimeMillis() - startTime;
        
        Map<String, Object> response = Map.of(
            "users", users,
            "count", users.size(),
            "totalUsers", userRepository.count(),
            "processingTimeMs", processingTime
        );
        
        return HttpResponse.ok(response)
                .header("Cache-Control", "public, max-age=300")
                .header("Surrogate-Control", "max-age=300");
    }
}