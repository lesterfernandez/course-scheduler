package handle

import (
	"encoding/json"
	"fmt"
	"net/http"
	"strings"

	"github.com/lesterfernandez/course-scheduler/backend/auth"
	"github.com/lesterfernandez/course-scheduler/backend/model"
	"golang.org/x/crypto/bcrypt"
)

type userCreds struct {
	Username string `json:"username"`
	Password string `json:"password"`
}

type authResponse struct {
	LoggedIn bool   `json:"loggedIn"`
	Username string `json:"username"`
	Token    string `json:"token"`
}

type loginResponse struct {
	authResponse
	Courses []model.Course `json:"courses"`
}

type errorMsg struct {
	ErrorMessage string `json:"errorMessage"`
}

func (s *Server) Register(w http.ResponseWriter, req *http.Request) {
	creds := userCreds{}
	decodeErr := json.NewDecoder(req.Body).Decode(&creds)

	if decodeErr != nil || creds.Username == "" || creds.Password == "" {
		respondWithError(w, "Invalid request!", 400)
		return
	}

	if s.User.UserExists(creds.Username) {
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

	s.User.CreateUser(&user)

	token, _ := auth.CreateToken(&user)

	w.WriteHeader(201)
	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(authResponse{true, user.Username, token})
	fmt.Printf("Registered user: %v\n", creds)
}

func (s *Server) LoginRoot(w http.ResponseWriter, req *http.Request) {
	if req.Method == http.MethodGet {
		s.ImplicitLogin(w, req)
	} else if req.Method == http.MethodPost {
		s.Login(w, req)
	}
}

func (s *Server) Login(w http.ResponseWriter, req *http.Request) {
	creds := userCreds{}
	decodeErr := json.NewDecoder(req.Body).Decode(&creds)

	if decodeErr != nil || creds.Username == "" || creds.Password == "" {
		respondWithError(w, "Invalid request!", 400)
		return
	}

	user, notFoundErr := s.User.UserByUsername(creds.Username)
	if notFoundErr != nil {
		respondWithError(w, "Wrong username or password!", 401)
		return
	}

	wrongPasswordErr := bcrypt.CompareHashAndPassword([]byte(user.PasswordHash), []byte(creds.Password))
	if wrongPasswordErr != nil {
		respondWithError(w, "Wrong username or password!", 401)
		return
	}

	courses := s.Course.Courses(user)

	token, _ := auth.CreateToken(user)

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(
		loginResponse{
			authResponse{true, user.Username, token},
			courses,
		})
	fmt.Printf("Logged in user: %v\n", creds)
}

func (s *Server) ImplicitLogin(w http.ResponseWriter, req *http.Request) {
	header := req.Header.Get("Authorization")
	splitHeader := strings.Split(header, " ")
	if len(splitHeader) != 2 || !strings.EqualFold(splitHeader[0], "Bearer") {
		respondWithError(w, "Not logged in!", 401)
		return
	}

	token := splitHeader[1]
	parsedToken, parseErr := auth.ParseToken(token)
	if parseErr != nil || !parsedToken.Valid {
		respondWithError(w, "Not logged in!", 401)
		return
	}

	username, _ := parsedToken.Claims.GetSubject()
	user, notFoundErr := s.User.UserByUsername(username)
	if notFoundErr != nil {
		respondWithError(w, "Not logged in!", 401)
		return
	}

	courses := s.Course.Courses(user)

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(
		loginResponse{
			authResponse{true, user.Username, token}, courses,
		})
	fmt.Printf("Logged in user: %v\n", username)
}
