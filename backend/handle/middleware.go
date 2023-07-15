package handle

import (
	"net/http"

	"github.com/lesterfernandez/course-scheduler/backend/auth"
)

func JwtFilter(h http.HandlerFunc) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		token, tokenErr := auth.ParseAuthHeader(r)
		if tokenErr != nil {
			respondWithError(w, "Not logged in!", 401)
			return
		}

		parsedToken, parseErr := auth.ParseToken(token)
		if parseErr != nil || !parsedToken.Valid {
			respondWithError(w, "Not logged in!", 401)
			return
		}

		h(w, r)
	}
}
