package com.claude.bakeoff

import com.claude.bakeoff.config.MongoConfig
import com.claude.bakeoff.repository.UserRepositoryImpl
import com.claude.bakeoff.routes.Routes
import com.claude.bakeoff.service.MathService
import zio.*
import zio.http.*

object Application extends ZIOAppDefault:

  def run =
    Server
      .serve(Routes.app)
      .provide(
        Server.defaultWithPort(8083),
        MathService.live,
        MongoConfig.layer,
        UserRepositoryImpl.layer
      )