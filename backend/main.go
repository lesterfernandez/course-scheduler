package main

import (
	"encoding/json"
	"fmt"
	"log"
	"net/http"
	"time"
)

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

type errorMsg struct {
	ErrorMessage string `json:"errorMessage"`
}

func main() {
	http.HandleFunc("/register", func(w http.ResponseWriter, req *http.Request) {
		dec := json.NewDecoder(req.Body)
		creds := struct {
			Username string `json:"username"`
			Password string `json:"password"`
		}{}
		err := dec.Decode(&creds)
		if err != nil || creds.Username == "" || creds.Password == "" {
			w.WriteHeader(400)
			badReqMsg, _ := json.Marshal(errorMsg{"Invalid request!"})
			w.Write(badReqMsg)
			return
		}
		fmt.Printf("%#v", creds)
	})
	log.Panicln(http.ListenAndServe(":8080", nil))
}
