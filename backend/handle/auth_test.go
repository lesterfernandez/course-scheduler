package handle

import (
	"testing"

	"github.com/lesterfernandez/course-scheduler/backend/model"
	"github.com/lesterfernandez/course-scheduler/backend/testdata"
)

func TestRegister(t *testing.T) {
	userRepo := &testdata.UserTestRepo{
		CreateUserMock: func(user *model.User) error { return nil },
		UserByUsernameMock: func(username string) (*model.User, error) {
			u := testdata.CreateTestUser()
			u.Username = username
			return u, nil
		},
		UserExistsMock: func(username string) bool { return true },
	}

	courseRepo := &testdata.CourseTestRepo{
		CoursesMock: func(user *model.User) []model.Course { return make([]model.Course, 0) },
	}

	s := ServerInit()
	s.User = userRepo
	s.Course = courseRepo
}
