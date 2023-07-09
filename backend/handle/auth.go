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

func (h *Handler) Register(w http.ResponseWriter, req *http.Request) {
	dec := json.NewDecoder(req.Body)
	creds := userCreds{}
	decodeErr := dec.Decode(&creds)

	if decodeErr != nil || creds.Username == "" || creds.Password == "" {
		respondWithError(w, "Invalid request!", 400)
		return
	}

	if h.Repo.UserExists(creds.Username) {
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

	h.Repo.CreateUser(&user)

	token, _ := auth.CreateToken(&user)

	res, _ := json.Marshal(authResponse{
		true, user.Username, token,
	})

	w.WriteHeader(201)
	w.Header().Set("Content-Type", "application/json")
	w.Write(res)

	fmt.Printf("Registered user: %v\n", creds)
}

func (h *Handler) LoginRoot(w http.ResponseWriter, req *http.Request) {
	if req.Method == http.MethodGet {
		h.ImplicitLogin(w, req)
	} else if req.Method == http.MethodPost {
		h.Login(w, req)
	}
}

func (h *Handler) Login(w http.ResponseWriter, req *http.Request) {
	dec := json.NewDecoder(req.Body)
	creds := userCreds{}
	decodeErr := dec.Decode(&creds)

	if decodeErr != nil || creds.Username == "" || creds.Password == "" {
		respondWithError(w, "Invalid request!", 400)
		return
	}

	user, notFoundErr := h.Repo.UserByUsername(creds.Username)
	if notFoundErr != nil {
		respondWithError(w, "Wrong username or password!", 401)
		return
	}

	wrongPasswordErr := bcrypt.CompareHashAndPassword([]byte(user.PasswordHash), []byte(creds.Password))
	if wrongPasswordErr != nil {
		respondWithError(w, "Wrong username or password!", 401)
		return
	}

	courses := h.Repo.Courses(user)

	token, _ := auth.CreateToken(user)

	res, _ := json.Marshal(loginResponse{
		authResponse{true, user.Username, token}, courses,
	})

	w.Header().Set("Content-Type", "application/json")
	w.Write(res)
	fmt.Printf("Logged in user: %v\n", creds)
}

func (h *Handler) ImplicitLogin(w http.ResponseWriter, req *http.Request) {
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
	user, notFoundErr := h.Repo.UserByUsername(username)
	if notFoundErr != nil {
		respondWithError(w, "Not logged in!", 401)
		return
	}

	courses := h.Repo.Courses(user)
	res, _ := json.Marshal(loginResponse{
		authResponse{true, user.Username, token}, courses,
	})

	w.Header().Set("Content-Type", "application/json")
	w.Write(res)
	fmt.Printf("Logged in user: %v\n", username)
}
