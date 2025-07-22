package models

import (
	"time"
	"go.mongodb.org/mongo-driver/bson/primitive"
)

type User struct {
	ID         primitive.ObjectID `json:"id" bson:"_id"`
	Name       string             `json:"name" bson:"name"`
	Email      string             `json:"email" bson:"email"`
	Age        int                `json:"age" bson:"age"`
	Department string             `json:"department" bson:"department"`
	Salary     float64            `json:"salary" bson:"salary"`
	Skills     []string           `json:"skills" bson:"skills"`
	CreatedAt  time.Time          `json:"createdAt" bson:"createdAt"`
}

type UsersResponse struct {
	Users           []User `json:"users"`
	Count           int    `json:"count"`
	TotalUsers      int64  `json:"totalUsers"`
	ProcessingTimeMs int64  `json:"processingTimeMs"`
}