package main

import (
	"log"

	"github.com/claude/bakeoff/golang/internal/config"
	"github.com/claude/bakeoff/golang/internal/handler"
	"github.com/claude/bakeoff/golang/internal/repository"
	"github.com/gin-gonic/gin"
)

func main() {
	gin.SetMode(gin.ReleaseMode)
	
	// Connect to MongoDB
	mongoClient, err := config.NewMongoClient()
	if err != nil {
		log.Fatal("Failed to connect to MongoDB:", err)
	}
	defer mongoClient.Disconnect(nil)

	// Initialize repositories
	userRepo := repository.NewUserRepository(mongoClient.Database("bakeoff"))
	
	router := gin.Default()
	h := handler.NewHandler(userRepo)

	router.GET("/health", h.Health)
	router.POST("/api/compute", h.Compute)
	router.GET("/api/users", h.GetUsers)

	log.Println("Starting Golang server on :8084")
	if err := router.Run(":8084"); err != nil {
		log.Fatal("Failed to start server:", err)
	}
}