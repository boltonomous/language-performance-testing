package services

import javax.inject.Singleton

@Singleton
class MathService {
  
  def compute(numbers: List[Double], operation: String): Double = {
    operation.toLowerCase match {
      case "sum" => numbers.sum
      case "average" => numbers.sum / numbers.length
      case "max" => numbers.maxOption.getOrElse(0.0)
      case "min" => numbers.minOption.getOrElse(0.0)
      case "fibonacci" => fibonacci(numbers.head.toInt)
      case _ => throw new IllegalArgumentException(s"Unsupported operation: $operation")
    }
  }
  
  private def fibonacci(n: Int): Double = {
    require(n >= 0, "Fibonacci input must be non-negative")
    require(n <= 40, "Fibonacci computation limited to n <= 40")
    
    if (n <= 1) n.toDouble
    else {
      var a = 0.0
      var b = 1.0
      
      for (_ <- 2 to n) {
        val temp = a + b
        a = b
        b = temp
      }
      
      b
    }
  }
}