package main

import (
	"log"
	"net/http"

	"github.com/lesterfernandez/course-scheduler/backend/data"
	"github.com/lesterfernandez/course-scheduler/backend/handle"
	"github.com/lesterfernandez/course-scheduler/backend/model"
	"github.com/rs/cors"
	"gorm.io/driver/postgres"
	"gorm.io/gorm"
	"gorm.io/gorm/logger"
)

func main() {
	db := setupDb()

	user := &data.UserData{Db: db}
	course := &data.CourseData{Db: db}

	s := handle.Server{User: user, Course: course}

	handle.ServerInit(&s, http.DefaultServeMux)

	c := setupCors()

	log.Panicln(http.ListenAndServe(":8080", c.Handler(http.DefaultServeMux)))
}

func setupDb() *gorm.DB {
	db, err := gorm.Open(postgres.Open("postgresql://postgres:postgres@localhost:5001"),
		&gorm.Config{
			Logger: logger.Default.LogMode(logger.Info),
		})
	if err != nil {
		panic("Could not connect to db")
	}
	db.AutoMigrate(&model.User{}, &model.Course{})
	return db
}

func setupCors() *cors.Cors {
	c := cors.New(cors.Options{
		AllowedHeaders: []string{"Authorization", "Content-Type"},
		Debug:          true,
	})
	return c
}
