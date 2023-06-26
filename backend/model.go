package main

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
	User            User
	// Status "AVAILABLE" | "COMPLETED" = "COMPLETED"
}
