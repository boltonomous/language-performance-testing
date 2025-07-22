package models

type ComputeRequest struct {
	Numbers   []float64 `json:"numbers" binding:"required"`
	Operation string    `json:"operation" binding:"required"`
}

type ComputeResponse struct {
	Result           float64 `json:"result"`
	Operation        string  `json:"operation"`
	InputSize        int     `json:"inputSize"`
	ProcessingTimeMs int64   `json:"processingTimeMs"`
}

type HealthResponse struct {
	Status    string `json:"status"`
	Service   string `json:"service"`
	Timestamp int64  `json:"timestamp"`
}