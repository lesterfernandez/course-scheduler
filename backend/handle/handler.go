package handle

import (
	"encoding/json"
	"net/http"

	"github.com/lesterfernandez/course-scheduler/backend/data"
)

type Handler struct {
	Repo data.Repo
}

func respondWithError(w http.ResponseWriter, msg string, code int) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(code)
	internalErrMsg, _ := json.Marshal(errorMsg{msg})
	w.Write(internalErrMsg)
}
