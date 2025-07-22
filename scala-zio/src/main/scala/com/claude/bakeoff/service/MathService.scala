package com.claude.bakeoff.service

import zio.*

trait MathService:
  def compute(numbers: List[Double], operation: String): IO[String, Double]

object MathService:
  val live: ULayer[MathService] = ZLayer.succeed(MathServiceImpl())

case class MathServiceImpl() extends MathService:
  def compute(numbers: List[Double], operation: String): IO[String, Double] =
    if numbers.isEmpty then
      ZIO.fail("Numbers list cannot be empty")
    else
      operation match
        case "sum" => ZIO.succeed(numbers.sum)
        case "multiply" => ZIO.succeed(numbers.product)
        case "mean" => ZIO.succeed(numbers.sum / numbers.length)
        case "max" => ZIO.succeed(numbers.max)
        case "min" => ZIO.succeed(numbers.min)
        case _ => ZIO.fail(s"Unsupported operation: $operation")