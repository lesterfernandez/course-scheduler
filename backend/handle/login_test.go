package handle

import (
	"bytes"
	"encoding/json"
	"io"
	"net/http"
	"net/http/httptest"
	"testing"

	"github.com/lesterfernandez/course-scheduler/backend/auth"
	"github.com/lesterfernandez/course-scheduler/backend/model"
	"github.com/lesterfernandez/course-scheduler/backend/testdata"
	"golang.org/x/crypto/bcrypt"
)

func TestLogin(t *testing.T) {
	creds := userCreds{
		Username: "Timmy",
		Password: "Password7328!!",
	}
	reqBody, _ := json.Marshal(creds)

	user := testdata.CreateTestUser(
		testdata.WithUsername(creds.Username),
		testdata.WithPassword(creds.Password),
	)

	userRepo := &testdata.UserTestRepo{
		CreateUserMock: func(user *model.User) error { return nil },
		UserByUsernameMock: func(username string) (*model.User, error) {
			if username == user.Username {
				return user, nil
			}
			u := testdata.CreateTestUser()
			return u, nil
		},
	}

	courseRepo := &testdata.CourseTestRepo{
		CoursesMock: func(user *model.User) []model.Course { return make([]model.Course, 0) },
	}

	mux := http.NewServeMux()
	ServerInit(&Server{
		User:   userRepo,
		Course: courseRepo,
	}, mux)

	t.Run("Login attempt", func(t *testing.T) {
		req := httptest.NewRequest(http.MethodPost, "/api/login", bytes.NewReader(reqBody))
		w := httptest.NewRecorder()
		mux.ServeHTTP(w, req)

		res := w.Result()
		resBody, _ := io.ReadAll(res.Body)
		loginRes := loginResponse{}
		unmarshalErr := json.Unmarshal(resBody, &loginRes)

		if unmarshalErr != nil {
			t.Fatal("Could not parse response:", unmarshalErr)
		}

		if loginRes.Username != "Timmy" || !loginRes.LoggedIn {
			t.FailNow()
		}

		if res.StatusCode != http.StatusOK {
			t.Fatal("Incorrect status code")
		}

		passDigest, _ := bcrypt.GenerateFromPassword([]byte(creds.Password), 10)
		user := model.User{
			Username:     creds.Username,
			PasswordHash: string(passDigest),
		}
		token, _ := auth.CreateToken(&user)

		if token != loginRes.Token {
			t.Fatal("Did not generate JWT correctly")
		}
	})

	t.Run("Implicit login attempt", func(t *testing.T) {
		passDigest, _ := bcrypt.GenerateFromPassword([]byte(creds.Password), 10)
		user := model.User{
			Username:     creds.Username,
			PasswordHash: string(passDigest),
		}
		token, _ := auth.CreateToken(&user)

		req := httptest.NewRequest(http.MethodGet, "/api/login", bytes.NewReader(reqBody))
		req.Header.Set("Authorization", "Bearer "+token)
		w := httptest.NewRecorder()
		mux.ServeHTTP(w, req)

		res := w.Result()
		resBody, _ := io.ReadAll(res.Body)
		loginRes := loginResponse{}
		unmarshalErr := json.Unmarshal(resBody, &loginRes)

		if unmarshalErr != nil {
			t.Fatal("Could not parse response:", unmarshalErr)
		}

		if loginRes.Username != "Timmy" || !loginRes.LoggedIn {
			t.FailNow()
		}

		if res.StatusCode != http.StatusOK {
			t.Fatal("Incorrect status code")
		}

		if token != loginRes.Token {
			t.Fatal("Did not generate JWT correctly")
		}
	})
}
