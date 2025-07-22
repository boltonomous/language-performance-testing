package com.claude.bakeoff.config

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients

object MongoConfig {
    
    fun createMongoClient(): MongoClient {
        val connectionString = System.getenv("MONGODB_URI") 
            ?: "mongodb://admin:password@mongodb:27017/bakeoff?authSource=admin"
        return MongoClients.create(connectionString)
    }
}