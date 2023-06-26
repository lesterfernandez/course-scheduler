package main

import (
	"encoding/json"
	"fmt"
	"net/http"
)

type errorMsg struct {
	ErrorMessage string `json:"errorMessage"`
}

type userCreds struct {
	Username string `json:"username"`
	Password string `json:"password"`
}

func RegisterHandler(w http.ResponseWriter, req *http.Request) {
	dec := json.NewDecoder(req.Body)

	creds := userCreds{}
	decodeErr := dec.Decode(&creds)

	if decodeErr != nil || creds.Username == "" || creds.Password == "" {
		w.WriteHeader(400)
		badReqMsg, _ := json.Marshal(errorMsg{"Invalid request!"})
		w.Write(badReqMsg)
		return
	}

	notFoundErr := db.First(&User{}, "username = ?", creds.Username)
	userFound := notFoundErr == nil
	if userFound {
		w.WriteHeader(409)
		badReqMsg, _ := json.Marshal(errorMsg{"Username taken!"})
		w.Write(badReqMsg)
		return
	}

	fmt.Printf("%#v", creds)
}
