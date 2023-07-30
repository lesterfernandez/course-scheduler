package main

import (
	"net/http"

	"github.com/lesterfernandez/course-scheduler/backend/data"
	"github.com/lesterfernandez/course-scheduler/backend/handle"
	"github.com/rs/cors"
)

func setupServer() http.Handler {
	db := data.SetupDb()

	user := &data.UserData{Db: db}
	course := &data.CourseData{Db: db}

	s := handle.Server{User: user, Course: course}

	handle.SetupHandlers(&s, http.DefaultServeMux)

	c := setupCors()

	return c.Handler(http.DefaultServeMux)
}

func setupCors() *cors.Cors {
	c := cors.New(cors.Options{
		AllowedHeaders: []string{"Authorization", "Content-Type"},
	})
	return c
}
