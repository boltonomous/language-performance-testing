package com.claude.bakeoff.model

import kotlinx.serialization.Serializable

@Serializable
data class UsersResponse(
    val users: List<User>,
    val count: Int,
    val totalUsers: Long,
    val processingTimeMs: Long
)