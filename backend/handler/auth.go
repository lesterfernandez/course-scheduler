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

type AuthService struct {
	Db *gorm.DB
}

type errorMsg struct {
	ErrorMessage string `json:"errorMessage"`
}

type userCreds struct {
	Username string `json:"username"`
	Password string `json:"password"`
}

func (auth *AuthService) RegisterUser(w http.ResponseWriter, req *http.Request) {
	dec := json.NewDecoder(req.Body)

	creds := userCreds{}
	decodeErr := dec.Decode(&creds)

	if decodeErr != nil || creds.Username == "" || creds.Password == "" {
		w.WriteHeader(400)
		badReqMsg, _ := json.Marshal(errorMsg{"Invalid request!"})
		w.Write(badReqMsg)
		return
	}

	if userExists(auth, creds.Username) {
		w.WriteHeader(409)
		badReqMsg, _ := json.Marshal(errorMsg{"Username taken!"})
		w.Write(badReqMsg)
		return
	}

	passDigest, hashErr := bcrypt.GenerateFromPassword([]byte(creds.Password), 10)
	if hashErr != nil {
		w.WriteHeader(500)
		internalErrMsg, _ := json.Marshal(errorMsg{"Something went wrong!"})
		fmt.Println(hashErr)
		w.Write(internalErrMsg)
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

func userExists(auth *AuthService, username string) bool {
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
