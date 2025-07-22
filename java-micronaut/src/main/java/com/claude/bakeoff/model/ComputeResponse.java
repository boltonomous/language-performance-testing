package com.claude.bakeoff.model;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public class ComputeResponse {
    
    private double result;
    private String operation;
    private int inputSize;
    private long processingTimeMs;
    
    public ComputeResponse() {}
    
    public ComputeResponse(double result, String operation, int inputSize, long processingTimeMs) {
        this.result = result;
        this.operation = operation;
        this.inputSize = inputSize;
        this.processingTimeMs = processingTimeMs;
    }
    
    public double getResult() {
        return result;
    }
    
    public void setResult(double result) {
        this.result = result;
    }
    
    public String getOperation() {
        return operation;
    }
    
    public void setOperation(String operation) {
        this.operation = operation;
    }
    
    public int getInputSize() {
        return inputSize;
    }
    
    public void setInputSize(int inputSize) {
        this.inputSize = inputSize;
    }
    
    public long getProcessingTimeMs() {
        return processingTimeMs;
    }
    
    public void setProcessingTimeMs(long processingTimeMs) {
        this.processingTimeMs = processingTimeMs;
    }
}