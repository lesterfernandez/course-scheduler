package testdata

import "github.com/lesterfernandez/course-scheduler/backend/model"

type UserTestRepo struct {
	UserCreateMock       func(user *model.User) error
	UserExistsMock       func(username string) bool
	UserByUsernameMock   func(username string) (*model.User, error)
	UserIdByUsernameMock func(username string) (uint, error)
}

func (u *UserTestRepo) UserCreate(user *model.User) error {
	return u.UserCreateMock(user)
}

func (u *UserTestRepo) UserExists(username string) bool {
	return u.UserExistsMock(username)
}

func (u *UserTestRepo) UserByUsername(username string) (*model.User, error) {
	return u.UserByUsernameMock(username)
}

func (u *UserTestRepo) UserIdByUsername(username string) (uint, error) {
	return u.UserIdByUsernameMock(username)
}

type CourseTestRepo struct {
	CoursesMock           func(user *model.User) []*model.Course
	CoursesByUsernameMock func(username string) []*model.Course
	CoursesCreateMock     func(courses []*model.Course) error
}

func (c *CourseTestRepo) Courses(user *model.User) []*model.Course {
	return c.CoursesMock(user)
}

func (c *CourseTestRepo) CoursesByUsername(username string) []*model.Course {
	return c.CoursesByUsernameMock(username)
}

func (c *CourseTestRepo) CoursesCreate(courses []*model.Course) error {
	return c.CoursesCreateMock(courses)
}
