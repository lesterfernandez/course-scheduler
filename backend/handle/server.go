package handle

import (
	"net/http"

	"github.com/lesterfernandez/course-scheduler/backend/data"
)

type Server struct {
	User   data.UserRepo
	Course data.CourseRepo
}

func SetupHandlers(s *Server, mux *http.ServeMux) {
	mux.HandleFunc("/api/auth/register", s.Register)
	mux.HandleFunc("/api/auth/login", s.LoginRoot)
	mux.HandleFunc("/api/schedule", s.CoursesRoot)
}
