package com.claude.bakeoff.service

import jakarta.inject.Singleton

@Singleton
open class MathService {
    
    open fun compute(numbers: List<Double>, operation: String): Double {
        return when (operation.lowercase()) {
            "sum" -> numbers.sum()
            "average" -> numbers.average()
            "max" -> numbers.maxOrNull() ?: 0.0
            "min" -> numbers.minOrNull() ?: 0.0
            "fibonacci" -> fibonacci(numbers.first().toInt())
            else -> throw IllegalArgumentException("Unsupported operation: $operation")
        }
    }
    
    private fun fibonacci(n: Int): Double {
        require(n >= 0) { "Fibonacci input must be non-negative" }
        require(n <= 40) { "Fibonacci computation limited to n <= 40" }
        
        if (n <= 1) return n.toDouble()
        
        var a = 0.0
        var b = 1.0
        
        for (i in 2..n) {
            val temp = a + b
            a = b
            b = temp
        }
        
        return b
    }
}