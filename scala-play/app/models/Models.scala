package models

import play.api.libs.json._

case class ComputeRequest(
  numbers: List[Double],
  operation: String
)

case class ComputeResponse(
  result: Double,
  operation: String,
  inputSize: Int,
  processingTimeMs: Long
)

case class HealthResponse(
  status: String,
  service: String,
  timestamp: Long
)

object ComputeRequest {
  implicit val format: Format[ComputeRequest] = Json.format[ComputeRequest]
}

object ComputeResponse {
  implicit val format: Format[ComputeResponse] = Json.format[ComputeResponse]
}

object HealthResponse {
  implicit val format: Format[HealthResponse] = Json.format[HealthResponse]
}