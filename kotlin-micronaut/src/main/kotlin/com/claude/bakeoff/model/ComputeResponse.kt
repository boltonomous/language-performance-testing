package com.claude.bakeoff.model

import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class ComputeResponse(
    val result: Double,
    val operation: String,
    val inputSize: Int,
    val processingTimeMs: Long
)