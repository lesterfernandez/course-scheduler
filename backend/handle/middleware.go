package handle

import (
	"net/http"

	"github.com/lesterfernandez/course-scheduler/backend/auth"
)

func JwtFilter(h http.HandlerFunc) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		_, parseJwtErr := auth.ParseTokenFromRequest(r)

		if parseJwtErr != nil {
			respondWithError(w, "Not logged in!", 401)
			return
		}

		h(w, r)
	}
}
