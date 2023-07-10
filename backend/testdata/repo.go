package testdata

import "github.com/lesterfernandez/course-scheduler/backend/model"

type UserTestRepo struct {
	CreateUserMock     func(user *model.User) error
	UserByUsernameMock func(username string) (*model.User, error)
	UserExistsMock     func(username string) bool
}

func (u *UserTestRepo) CreateUser(user *model.User) error {
	return u.CreateUserMock(user)
}

func (u *UserTestRepo) UserByUsername(username string) (*model.User, error) {
	return u.UserByUsernameMock(username)
}

func (u *UserTestRepo) UserExists(username string) bool {
	return u.UserExistsMock(username)
}

type CourseTestRepo struct {
	CoursesMock func(user *model.User) []model.Course
}

func (c *CourseTestRepo) Courses(user *model.User) []model.Course {
	return c.CoursesMock(user)
}
