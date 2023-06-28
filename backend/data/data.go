package data

import (
	"github.com/lesterfernandez/course-scheduler/backend/model"
	"gorm.io/gorm"
)

type Repo interface {
	UserByUsername(username string) (*model.User, error)
	UserExists(username string) bool
	Courses(user *model.User) []model.Course
	CreateUser(user *model.User) error
}

type DataRepo struct {
	Db *gorm.DB
}

func (data *DataRepo) UserByUsername(username string) (*model.User, error) {
	user := model.User{}
	notFoundErr := data.Db.First(&user, "username = ?", username).Error
	return &user, notFoundErr
}

func (data *DataRepo) UserExists(username string) bool {
	_, notFoundErr := data.UserByUsername(username)
	return notFoundErr == nil
}

func (data *DataRepo) Courses(user *model.User) []model.Course {
	var courses []model.Course
	data.Db.Model(user).Association("Courses").Find(&courses)
	return courses
}

func (data *DataRepo) CreateUser(user *model.User) error {
	return data.Db.Create(user).Error
}
