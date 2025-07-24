package com.claude.bakeoff.repository

import com.claude.bakeoff.model.User
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : MongoRepository<User, String> {
    fun findByDepartment(department: String): List<User>
}