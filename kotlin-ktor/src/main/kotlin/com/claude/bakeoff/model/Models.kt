package com.claude.bakeoff.model

import kotlinx.serialization.Serializable

@Serializable
data class ComputeRequest(
    val numbers: List<Double>,
    val operation: String
)

@Serializable
data class ComputeResponse(
    val result: Double,
    val operation: String,
    val inputSize: Int,
    val processingTimeMs: Long
)

@Serializable
data class HealthResponse(
    val status: String,
    val service: String,
    val timestamp: Long
)