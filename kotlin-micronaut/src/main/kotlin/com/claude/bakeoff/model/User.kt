package com.claude.bakeoff.model

import io.micronaut.serde.annotation.Serdeable
import java.time.Instant

@Serdeable
data class User(
    val id: String,
    val name: String,
    val email: String,
    val age: Int,
    val department: String,
    val salary: Double,
    val skills: List<String>,
    val createdAt: Instant
)