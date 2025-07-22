package com.claude.bakeoff.model

import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class User(
    val id: String,
    val name: String,
    val email: String,
    val age: Int,
    val department: String,
    val salary: Double,
    val skills: List<String>,
    val createdAt: String // Using String for Instant serialization
)