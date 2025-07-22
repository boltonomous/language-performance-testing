package com.claude.bakeoff.model

import zio.json.*

case class User(
  id: String,
  name: String,
  email: String,
  age: Int,
  department: String,
  salary: Double,
  skills: List[String],
  createdAt: String
)

object User:
  given JsonCodec[User] = DeriveJsonCodec.gen[User]

case class UsersResponse(
  users: List[User],
  count: Int,
  totalUsers: Long,
  processingTimeMs: Long
)

object UsersResponse:
  given JsonCodec[UsersResponse] = DeriveJsonCodec.gen[UsersResponse]