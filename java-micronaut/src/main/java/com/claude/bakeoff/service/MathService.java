package com.claude.bakeoff.service;

import jakarta.inject.Singleton;
import java.util.List;

@Singleton
public class MathService {
    
    public double compute(List<Double> numbers, String operation) {
        return switch (operation.toLowerCase()) {
            case "sum" -> sum(numbers);
            case "average" -> average(numbers);
            case "max" -> max(numbers);
            case "min" -> min(numbers);
            case "fibonacci" -> fibonacci(numbers.get(0).intValue());
            default -> throw new IllegalArgumentException("Unsupported operation: " + operation);
        };
    }
    
    private double sum(List<Double> numbers) {
        return numbers.stream().mapToDouble(Double::doubleValue).sum();
    }
    
    private double average(List<Double> numbers) {
        return numbers.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    }
    
    private double max(List<Double> numbers) {
        return numbers.stream().mapToDouble(Double::doubleValue).max().orElse(0.0);
    }
    
    private double min(List<Double> numbers) {
        return numbers.stream().mapToDouble(Double::doubleValue).min().orElse(0.0);
    }
    
    private double fibonacci(int n) {
        if (n <= 1) return n;
        if (n > 40) throw new IllegalArgumentException("Fibonacci computation limited to n <= 40");
        
        double a = 0, b = 1;
        for (int i = 2; i <= n; i++) {
            double temp = a + b;
            a = b;
            b = temp;
        }
        return b;
    }
}