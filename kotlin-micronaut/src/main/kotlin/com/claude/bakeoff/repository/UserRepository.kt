package com.claude.bakeoff.repository

import com.claude.bakeoff.model.User
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters
import jakarta.inject.Singleton
import org.bson.Document

@Singleton
open class UserRepository(private val mongoClient: MongoClient) {
    
    private val collection: MongoCollection<Document> = 
        mongoClient.getDatabase("bakeoff").getCollection("users")
    
    open fun findAll(): List<User> {
        return collection.find().map { documentToUser(it) }.toList()
    }
    
    open fun findByDepartment(department: String): List<User> {
        return collection.find(Filters.eq("department", department))
            .map { documentToUser(it) }.toList()
    }
    
    open fun findByEmail(email: String): User? {
        return collection.find(Filters.eq("email", email)).first()?.let { documentToUser(it) }
    }
    
    open fun count(): Long {
        return collection.countDocuments()
    }
    
    private fun documentToUser(doc: Document): User {
        return User(
            id = doc.getObjectId("_id").toString(),
            name = doc.getString("name"),
            email = doc.getString("email"),
            age = doc.getInteger("age", 0),
            department = doc.getString("department"),
            salary = when (val salaryValue = doc.get("salary")) {
                is Int -> salaryValue.toDouble()
                is Double -> salaryValue
                else -> 0.0
            },
            skills = doc.getList("skills", String::class.java) ?: emptyList(),
            createdAt = doc.getDate("createdAt").toInstant()
        )
    }
}