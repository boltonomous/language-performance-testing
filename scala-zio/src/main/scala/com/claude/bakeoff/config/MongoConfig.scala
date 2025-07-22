package com.claude.bakeoff.config

import com.mongodb.client.{MongoClient, MongoClients}
import zio.*

object MongoConfig:
  val layer: ZLayer[Any, Nothing, MongoClient] = ZLayer.scoped {
    ZIO.acquireRelease(
      ZIO.succeed {
        val connectionString = sys.env.getOrElse("MONGODB_URI", 
          "mongodb://admin:password@mongodb:27017/bakeoff?authSource=admin")
        MongoClients.create(connectionString)
      }
    )(client => ZIO.succeed(client.close()))
  }