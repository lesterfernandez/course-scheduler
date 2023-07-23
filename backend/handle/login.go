package handle

import (
	"encoding/json"
	"fmt"
	"net/http"

	"github.com/lesterfernandez/course-scheduler/backend/auth"
	"golang.org/x/crypto/bcrypt"
)

func (s *Server) LoginRoot(w http.ResponseWriter, req *http.Request) {
	if req.Method == http.MethodGet {
		JwtFilter(s.ImplicitLogin)(w, req)
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

	courses := s.Course.CoursesByUserId(user.ID)
	schedule := scheduleDto{}
	schedule.fromCourses(courses)

	token, _ := auth.CreateToken(user)

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(
		loginResponse{
			authResponse{true, user.Username, token},
			schedule.Courses,
		})
	fmt.Printf("Logged in user: %v\n", creds)
}

func (s *Server) ImplicitLogin(w http.ResponseWriter, req *http.Request) {
	token, tokenErr := auth.ParseAuthHeader(req)
	if tokenErr != nil {
		respondWithError(w, "Not logged in!", 401)
		return
	}

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

	courses := s.Course.CoursesByUserId(user.ID)
	schedule := scheduleDto{}
	schedule.fromCourses(courses)

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(
		loginResponse{
			authResponse{true, user.Username, token},
			schedule.Courses,
		})

	fmt.Printf("Logged in user: %v\n", username)
}
