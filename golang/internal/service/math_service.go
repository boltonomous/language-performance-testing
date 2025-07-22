package service

import (
	"fmt"
	"math"
)

type MathService struct{}

func NewMathService() *MathService {
	return &MathService{}
}

func (s *MathService) Compute(numbers []float64, operation string) (float64, error) {
	if len(numbers) == 0 {
		return 0, fmt.Errorf("numbers list cannot be empty")
	}

	switch operation {
	case "sum":
		return s.sum(numbers), nil
	case "multiply":
		return s.multiply(numbers), nil
	case "mean":
		return s.mean(numbers), nil
	case "max":
		return s.max(numbers), nil
	case "min":
		return s.min(numbers), nil
	default:
		return 0, fmt.Errorf("unsupported operation: %s", operation)
	}
}

func (s *MathService) sum(numbers []float64) float64 {
	total := 0.0
	for _, num := range numbers {
		total += num
	}
	return total
}

func (s *MathService) multiply(numbers []float64) float64 {
	result := 1.0
	for _, num := range numbers {
		result *= num
	}
	return result
}

func (s *MathService) mean(numbers []float64) float64 {
	return s.sum(numbers) / float64(len(numbers))
}

func (s *MathService) max(numbers []float64) float64 {
	maxVal := numbers[0]
	for _, num := range numbers[1:] {
		maxVal = math.Max(maxVal, num)
	}
	return maxVal
}

func (s *MathService) min(numbers []float64) float64 {
	minVal := numbers[0]
	for _, num := range numbers[1:] {
		minVal = math.Min(minVal, num)
	}
	return minVal
}