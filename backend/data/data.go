package data

import (
	"github.com/lesterfernandez/course-scheduler/backend/model"
	"gorm.io/gorm"
)

type Repo struct {
	Db *gorm.DB
}

func (r *Repo) UserByUsername(username string) (*model.User, error) {
	user := model.User{}
	notFoundErr := r.Db.First(&user, "username = ?", username).Error
	return &user, notFoundErr
}

func (r *Repo) UserExists(username string) bool {
	_, notFoundErr := r.UserByUsername(username)
	return notFoundErr == nil
}

func (r *Repo) Courses(user *model.User) []model.Course {
	var courses []model.Course
	r.Db.Model(user).Association("Courses").Find(&courses)
	return courses
}

func (r *Repo) CreateUser(user *model.User) error {
	return r.Db.Create(user).Error
}
