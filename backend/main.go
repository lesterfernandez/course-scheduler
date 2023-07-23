package main

import (
	"log"
	"net/http"

	"github.com/lesterfernandez/course-scheduler/backend/data"
	"github.com/lesterfernandez/course-scheduler/backend/handle"
	"github.com/rs/cors"
)

func main() {
	db := data.SetupDb()

	user := &data.UserData{Db: db}
	course := &data.CourseData{Db: db}

	s := handle.Server{User: user, Course: course}

	handle.ServerInit(&s, http.DefaultServeMux)

	c := setupCors()

	log.Panicln(http.ListenAndServe(":8080", c.Handler(http.DefaultServeMux)))
}

func setupCors() *cors.Cors {
	c := cors.New(cors.Options{
		AllowedHeaders: []string{"Authorization", "Content-Type"},
	})
	return c
}
