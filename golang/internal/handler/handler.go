package handler

import (
	"context"
	"net/http"
	"time"

	"github.com/claude/bakeoff/golang/internal/models"
	"github.com/claude/bakeoff/golang/internal/repository"
	"github.com/claude/bakeoff/golang/internal/service"
	"github.com/gin-gonic/gin"
)

type Handler struct {
	mathService *service.MathService
	userRepo    *repository.UserRepository
}

func NewHandler(userRepo *repository.UserRepository) *Handler {
	return &Handler{
		mathService: service.NewMathService(),
		userRepo:    userRepo,
	}
}

func (h *Handler) Health(c *gin.Context) {
	response := models.HealthResponse{
		Status:    "OK",
		Service:   "golang",
		Timestamp: time.Now().UnixMilli(),
	}
	c.JSON(http.StatusOK, response)
}

func (h *Handler) Compute(c *gin.Context) {
	start := time.Now()

	var request models.ComputeRequest
	if err := c.ShouldBindJSON(&request); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": "Invalid JSON: " + err.Error()})
		return
	}

	if len(request.Numbers) == 0 {
		c.JSON(http.StatusBadRequest, gin.H{"error": "Numbers list cannot be empty"})
		return
	}

	if request.Operation == "" {
		c.JSON(http.StatusBadRequest, gin.H{"error": "Operation cannot be blank"})
		return
	}

	result, err := h.mathService.Compute(request.Numbers, request.Operation)
	if err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}

	processingTime := time.Since(start).Milliseconds()

	response := models.ComputeResponse{
		Result:           result,
		Operation:        request.Operation,
		InputSize:        len(request.Numbers),
		ProcessingTimeMs: processingTime,
	}

	c.JSON(http.StatusOK, response)
}

func (h *Handler) GetUsers(c *gin.Context) {
	start := time.Now()
	ctx := context.Background()
	
	department := c.Query("department")
	
	var users []models.User
	var err error
	
	if department == "" {
		users, err = h.userRepo.FindAll(ctx)
	} else {
		users, err = h.userRepo.FindByDepartment(ctx, department)
	}
	
	if err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
		return
	}
	
	totalUsers, err := h.userRepo.Count(ctx)
	if err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
		return
	}
	
	processingTime := time.Since(start).Milliseconds()
	
	response := models.UsersResponse{
		Users:           users,
		Count:           len(users),
		TotalUsers:      totalUsers,
		ProcessingTimeMs: processingTime,
	}
	
	c.JSON(http.StatusOK, response)
}