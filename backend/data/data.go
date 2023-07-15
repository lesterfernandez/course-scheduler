package data

import (
	"github.com/lesterfernandez/course-scheduler/backend/model"
	"gorm.io/gorm"
)

type UserRepo interface {
	CreateUser(user *model.User) error
	UserByUsername(username string) (*model.User, error)
	UserExists(username string) bool
}

type CourseRepo interface {
	Courses(user *model.User) []model.Course
	CoursesByUsername(username string) []model.Course
}

type UserData struct {
	Db *gorm.DB
}

type CourseData struct {
	Db *gorm.DB
}

func (data *UserData) UserByUsername(username string) (*model.User, error) {
	user := model.User{}
	notFoundErr := data.Db.First(&user, "username = ?", username).Error
	return &user, notFoundErr
}

func (data *UserData) UserExists(username string) bool {
	_, notFoundErr := data.UserByUsername(username)
	return notFoundErr == nil
}

func (data *UserData) CreateUser(user *model.User) error {
	return data.Db.Create(user).Error
}

func (data *CourseData) Courses(user *model.User) []model.Course {
	var courses []model.Course
	data.Db.Model(user).Association("Courses").Find(&courses)
	return courses
}

func (data *CourseData) CoursesByUsername(username string) []model.Course {
	var courses []model.Course
	data.Db.Joins("INNER JOIN users ON users.id = courses.user_id").Where("users.username = ?", username).Find(&courses)
	return courses
}
