package com.claude.bakeoff

import com.claude.bakeoff.config.MongoConfig
import com.claude.bakeoff.repository.UserRepository
import com.claude.bakeoff.routing.configureRouting
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import kotlinx.serialization.json.Json

fun main() {
    embeddedServer(Netty, port = 8082, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
        })
    }
    
    install(StatusPages) {
        exception<IllegalArgumentException> { call, cause ->
            call.respond(
                status = io.ktor.http.HttpStatusCode.BadRequest,
                message = mapOf("error" to (cause.message ?: "Invalid request"))
            )
        }
    }
    
    val mongoClient = MongoConfig.createMongoClient()
    val userRepository = UserRepository(mongoClient)
    
    configureRouting(userRepository)
}