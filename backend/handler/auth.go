package handler

import (
	"encoding/json"
	"fmt"
	"net/http"
	"time"

	"github.com/golang-jwt/jwt/v5"
	"github.com/lesterfernandez/course-scheduler/backend/model"
	"golang.org/x/crypto/bcrypt"
	"gorm.io/gorm"
)

type Auth struct {
	Db *gorm.DB
}

type errorMsg struct {
	ErrorMessage string `json:"errorMessage"`
}

type userCreds struct {
	Username string `json:"username"`
	Password string `json:"password"`
}

func (auth *Auth) Register(w http.ResponseWriter, req *http.Request) {
	dec := json.NewDecoder(req.Body)

	creds := userCreds{}
	decodeErr := dec.Decode(&creds)

	if decodeErr != nil || creds.Username == "" || creds.Password == "" {
		respondWithError(w, "Invalid request!", 400)
		return
	}

	if auth.userExists(creds.Username) {
		respondWithError(w, "Username taken!", 409)
		return
	}

	passDigest, hashErr := bcrypt.GenerateFromPassword([]byte(creds.Password), 10)
	if hashErr != nil {
		respondWithError(w, "Something went wrong!", 500)
		return
	}

	user := model.User{
		Username:     creds.Username,
		PasswordHash: string(passDigest),
	}

	auth.Db.Create(&user)

	token, _ := createToken(&user)

	response, _ := json.Marshal(struct {
		LoggedIn bool   `json:"loggedIn"`
		Username string `json:"username"`
		Token    string `json:"token"`
	}{
		true, user.Username, token,
	})

	w.WriteHeader(201)
	w.Write(response)

	fmt.Printf("Registered user: %v\n", creds)
}

func (auth *Auth) userExists(username string) bool {
	notFoundErr := auth.Db.First(&model.User{}, "username = ?", username).Error
	return notFoundErr == nil
}

func createToken(u *model.User) (string, error) {
	claims := &jwt.RegisteredClaims{
		Subject:   u.Username,
		IssuedAt:  jwt.NewNumericDate(time.Now()),
		ExpiresAt: jwt.NewNumericDate(time.Now().Add(time.Hour * 24 * 7)),
	}
	t := jwt.NewWithClaims(jwt.SigningMethodHS256, claims)
	return t.SignedString([]byte("totally secret string here..."))
}

func respondWithError(w http.ResponseWriter, msg string, code int) {
	w.WriteHeader(code)
	internalErrMsg, _ := json.Marshal(errorMsg{msg})
	w.Write(internalErrMsg)
}
