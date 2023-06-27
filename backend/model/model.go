package model

import "time"

type User struct {
	ID                     uint
	CreatedAt, UpdatedAt   time.Time
	Username, PasswordHash string
	Courses                []Course
}

type Course struct {
	ID              uint
	Uuid            string
	Letters, Number string
	CourseIndex     uint
	Status          string
	UserID          uint
	// Status "AVAILABLE" | "COMPLETED" = "COMPLETED"
}
