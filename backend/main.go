package main

import (
	"log"
	"net/http"

	"github.com/lesterfernandez/course-scheduler/backend/handler"
	"github.com/lesterfernandez/course-scheduler/backend/model"
	"gorm.io/driver/postgres"
	"gorm.io/gorm"
)

func main() {
	db, err := gorm.Open(postgres.Open("postgresql://postgres:postgres@localhost:5001"))
	if err != nil {
		panic("Could not connect to db")
	}

	db.AutoMigrate(&model.User{}, &model.Course{})

	auth := handler.AuthService{Db: db}

	http.HandleFunc("/register", auth.RegisterUser)

	log.Panicln(http.ListenAndServe(":8080", nil))
}