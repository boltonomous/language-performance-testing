package com.claude.bakeoff.model

import zio.json.*

case class ComputeRequest(
  numbers: List[Double],
  operation: String
) derives JsonCodec

case class ComputeResponse(
  result: Double,
  operation: String,
  inputSize: Int,
  processingTimeMs: Long
) derives JsonCodec

case class HealthResponse(
  status: String,
  service: String, 
  timestamp: Long
) derives JsonCodec