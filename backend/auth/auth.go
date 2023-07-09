package auth

import (
	"time"

	"github.com/golang-jwt/jwt/v5"
	"github.com/lesterfernandez/course-scheduler/backend/model"
)

const jwtSecret = "totally secret string here..."

func CreateToken(u *model.User) (string, error) {
	claims := &jwt.RegisteredClaims{
		Subject:   u.Username,
		IssuedAt:  jwt.NewNumericDate(time.Now()),
		ExpiresAt: jwt.NewNumericDate(time.Now().Add(time.Hour * 24 * 7)),
	}
	t := jwt.NewWithClaims(jwt.SigningMethodHS256, claims)
	return t.SignedString([]byte(jwtSecret))
}

func ParseToken(token string) (*jwt.Token, error) {
	validMethods := []string{jwt.SigningMethodHS256.Name}
	parser := jwt.NewParser(jwt.WithValidMethods(validMethods), jwt.WithIssuedAt())
	parsedToken, parseErr := parser.Parse(token, func(t *jwt.Token) (interface{}, error) {
		return []byte(jwtSecret), nil
	})
	return parsedToken, parseErr
}
