package com.claude.bakeoff.model

import io.micronaut.serde.annotation.Serdeable
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull

@Serdeable
data class ComputeRequest(
    @field:NotNull
    @field:NotEmpty
    val numbers: List<Double>,
    
    @field:NotNull
    val operation: String
)