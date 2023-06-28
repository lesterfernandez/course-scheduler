package main

import (
	"log"
	"net/http"

	"github.com/lesterfernandez/course-scheduler/backend/data"
	"github.com/lesterfernandez/course-scheduler/backend/handle"
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

	data := data.Repo{Db: db}
	h := handle.Handler{Data: data}

	http.HandleFunc("/register", h.Register)
	http.HandleFunc("/login", h.Login)

	log.Panicln(http.ListenAndServe(":8080", nil))
}
