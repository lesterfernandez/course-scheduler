package model

import (
	crand "crypto/rand"
	"encoding/hex"
	"fmt"
	"math/rand"
	"time"

	"golang.org/x/crypto/bcrypt"
)

func CreateTestUser() *User {
	bytes := make([]byte, 4)
	crand.Read(bytes)

	passDigest, _ := bcrypt.GenerateFromPassword(bytes, 10)

	return &User{
		ID:           uint(rand.Intn(100)),
		CreatedAt:    time.Now(),
		UpdatedAt:    time.Now(),
		Username:     "Bob",
		PasswordHash: string(passDigest),
	}
}

func CreateTestCourse() *Course {
	bytes := make([]byte, 4)
	crand.Read(bytes)

	status := "AVAILABLE"
	if rand.Intn(2) == 1 {
		status = "COMPLETED"
	}

	return &Course{
		ID:          uint(rand.Intn(100)),
		Uuid:        hex.EncodeToString(bytes),
		Letters:     "COP",
		Number:      fmt.Sprint(rand.Intn(999)),
		Status:      status,
		CourseIndex: uint(rand.Intn(10)),
		UserID:      uint(rand.Intn(100)),
	}
}
