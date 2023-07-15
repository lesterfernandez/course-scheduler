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

func TestRegister(t *testing.T) {
	t.Run("New user is created", func(t *testing.T) {
		userRepo := &testdata.UserTestRepo{
			CreateUserMock: func(user *model.User) error { return nil },
			UserByUsernameMock: func(username string) (*model.User, error) {
				u := testdata.CreateTestUser()
				u.Username = username
				return u, nil
			},
			UserExistsMock: func(username string) bool { return false },
		}
		courseRepo := &testdata.CourseTestRepo{
			CoursesMock: func(user *model.User) []model.Course { return make([]model.Course, 0) },
		}
		mux := http.NewServeMux()
		ServerInit(&Server{
			User:   userRepo,
			Course: courseRepo,
		}, mux)

		creds := userCreds{
			Username: "Bob",
			Password: "Password123",
		}
		reqBody, _ := json.Marshal(creds)
		req := httptest.NewRequest(http.MethodGet, "/api/register", bytes.NewReader(reqBody))
		w := httptest.NewRecorder()
		mux.ServeHTTP(w, req)

		res := w.Result()
		resBody, _ := io.ReadAll(res.Body)
		authRes := authResponse{}
		unmarshalErr := json.Unmarshal(resBody, &authRes)

		if unmarshalErr != nil {
			t.Fatal("Could not parse response:", unmarshalErr)
		}

		if authRes.Username != "Bob" || !authRes.LoggedIn {
			t.FailNow()
		}

		if w.Result().StatusCode != http.StatusCreated {
			t.Fatal("Incorrect status code")
		}

		passDigest, _ := bcrypt.GenerateFromPassword([]byte(creds.Password), 10)
		user := model.User{
			Username:     creds.Username,
			PasswordHash: string(passDigest),
		}
		token, _ := auth.CreateToken(&user)

		if token != authRes.Token {
			t.Fatal("Did not generate JWT correctly")
		}
	})

	t.Run("Register with existing username", func(t *testing.T) {
		userRepo := &testdata.UserTestRepo{
			CreateUserMock: func(user *model.User) error { return nil },
			UserByUsernameMock: func(username string) (*model.User, error) {
				u := testdata.CreateTestUser()
				u.Username = username
				return u, nil
			},
			UserExistsMock: func(username string) bool { return true },
		}
		mux := http.NewServeMux()
		ServerInit(&Server{
			User: userRepo,
		}, mux)

		reqBody, _ := json.Marshal(userCreds{
			Username: "Bob",
			Password: "Password123",
		})

		req := httptest.NewRequest(http.MethodGet, "/api/register", bytes.NewReader(reqBody))
		w := httptest.NewRecorder()
		mux.ServeHTTP(w, req)

		res := w.Result()
		resBody, _ := io.ReadAll(res.Body)
		authRes := errorMsg{}
		unmarshalErr := json.Unmarshal(resBody, &authRes)

		if unmarshalErr != nil {
			t.Fatal("Could not parse response:", unmarshalErr)
		}

		if res.StatusCode != http.StatusConflict {
			t.Fatal("Incorrect status code")
		}
	})
}

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
