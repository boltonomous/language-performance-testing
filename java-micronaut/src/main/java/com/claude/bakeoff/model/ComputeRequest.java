package com.claude.bakeoff.model;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Serdeable
public class ComputeRequest {
    
    @NotNull
    @NotEmpty
    private List<Double> numbers;
    
    @NotNull
    private String operation;
    
    public ComputeRequest() {}
    
    public ComputeRequest(List<Double> numbers, String operation) {
        this.numbers = numbers;
        this.operation = operation;
    }
    
    public List<Double> getNumbers() {
        return numbers;
    }
    
    public void setNumbers(List<Double> numbers) {
        this.numbers = numbers;
    }
    
    public String getOperation() {
        return operation;
    }
    
    public void setOperation(String operation) {
        this.operation = operation;
    }
}