package handle

import (
	"encoding/json"
	"net/http"

	"github.com/lesterfernandez/course-scheduler/backend/auth"
	"github.com/lesterfernandez/course-scheduler/backend/model"
)

type schedule struct {
	Courses []model.Course `json:"courses"`
}

func (s *Server) CoursesRoot(w http.ResponseWriter, r *http.Request) {
	if r.Method == http.MethodGet {
		s.CoursesGet(w, r)
	} else if r.Method == http.MethodPost {
		s.CoursesPost(w, r)
	}
}

func (s *Server) CoursesGet(w http.ResponseWriter, r *http.Request) {
	jwt, jwtParseErr := auth.ParseTokenFromRequest(r)
	if jwtParseErr != nil {
		respondWithError(w, "Not logged in!", 401)
		return
	}

	username, _ := jwt.Claims.GetSubject()

	courses := s.Course.CoursesByUsername(username)
	resBody := schedule{
		courses,
	}
	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(resBody)
}

func (s *Server) CoursesPost(w http.ResponseWriter, r *http.Request) {
	schedule := schedule{}
	parseBodyErr := json.NewDecoder(r.Body).Decode(&schedule)

	if parseBodyErr != nil {
		respondWithError(w, "Something went wrong!", 400)
		return
	}

}
