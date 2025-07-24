package com.claude.bakeoff.model

import kotlinx.serialization.Serializable
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

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

@Document("users")
@Serializable
data class User(
    @Id
    val id: String? = null,
    val name: String,
    val email: String,
    val age: Int,
    val department: String,
    val salary: Double,
    val skills: List<String>,
    val createdAt: String? = null
)

@Serializable
data class UsersResponse(
    val users: List<User>,
    val count: Int,
    val totalUsers: Long,
    val processingTimeMs: Long
)