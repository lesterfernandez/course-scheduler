package handle

import (
	"encoding/json"
	"net/http"

	"github.com/lesterfernandez/course-scheduler/backend/data"
)

type Server struct {
	User   data.UserRepo
	Course data.CourseRepo
}

func ServerInit(s *Server, mux *http.ServeMux) {
	mux.HandleFunc("/api/register", s.Register)
	mux.HandleFunc("/api/login", s.LoginRoot)
}

func respondWithError(w http.ResponseWriter, msg string, code int) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(code)
	internalErrMsg, _ := json.Marshal(errorMsg{msg})
	w.Write(internalErrMsg)
}
