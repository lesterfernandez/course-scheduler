package handle

import (
	"encoding/json"
	"fmt"
	"net/http"

	"github.com/lesterfernandez/course-scheduler/backend/auth"
	"github.com/lesterfernandez/course-scheduler/backend/model"
)

type schedule struct {
	Courses []model.Course `json:"courses"`
}

func (s *Server) CoursesRoot(w http.ResponseWriter, r *http.Request) {
	if r.Method == http.MethodGet {
		s.CoursesGet(w, r)
	} else if r.Method == http.MethodPost {
		s.CoursesPost(w, r)
	}
}

func (s *Server) CoursesGet(w http.ResponseWriter, r *http.Request) {
	jwt, jwtParseErr := auth.ParseTokenFromRequest(r)
	if jwtParseErr != nil {
		respondWithError(w, "Not logged in!", 401)
		return
	}

	username, _ := jwt.Claims.GetSubject()

	courses := s.Course.CoursesByUsername(username)
	resBody := schedule{
		courses,
	}
	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(resBody)
}

type courseDto struct {
	Uuid          string   `json:"uuid"`
	Letters       string   `json:"letters"`
	Number        string   `json:"number"`
	CourseIndex   int      `json:"courseIndex"`
	Status        string   `json:"status"`
	Prerequisites []string `json:"prerequisites"`
}

type scheduleDto struct {
	Courses []courseDto `json:"courses"`
}

func (s *scheduleDto) fromCourses(courses []*model.Course, uuidToPrereqs map[string][]string) {
	s.Courses = make([]courseDto, 0, len(courses))
	for _, course := range courses {
		course := courseDto{
			Uuid:          course.Uuid,
			Letters:       course.Letters,
			Number:        course.Number,
			Status:        course.Status,
			CourseIndex:   int(course.CourseIndex),
			Prerequisites: uuidToPrereqs[course.Uuid],
		}
		s.Courses = append(s.Courses, course)
	}
}

func buildMaps(s *scheduleDto) (map[string]*model.Course, map[string][]string) {
	courseMap := make(map[string]*model.Course, len(s.Courses))
	uuidToPrereqs := make(map[string][]string)
	for _, courseDto := range s.Courses {
		course := model.Course{
			Uuid:        courseDto.Uuid,
			Letters:     courseDto.Letters,
			Number:      courseDto.Number,
			CourseIndex: uint(courseDto.CourseIndex),
			Status:      courseDto.Status,
		}
		courseMap[course.Uuid] = &course
		uuidToPrereqs[courseDto.Uuid] = courseDto.Prerequisites
	}
	return courseMap, uuidToPrereqs
}

func initializeGraph(s *scheduleDto, courseMap map[string]*model.Course) (map[*model.Course][]*model.Course, []*model.Course, map[*model.Course]int) {
	adjList := make(map[*model.Course][]*model.Course)
	inDegree := make(map[*model.Course]int)
	available := make([]*model.Course, 0)
	for _, courseDto := range s.Courses {
		course := courseMap[courseDto.Uuid]
		for _, prereqUuid := range courseDto.Prerequisites {
			prereq := courseMap[prereqUuid]
			adjList[prereq] = append(adjList[prereq], course)
			inDegree[course]++
		}
		if inDegree[course] == 0 {
			available = append(available, course)
		}
	}
	return adjList, available, inDegree
}

func sortCourses(adjList map[*model.Course][]*model.Course, available []*model.Course, inDegree map[*model.Course]int, dst []*model.Course) []*model.Course {
	fmt.Printf("------ %#v %[1]p \n", dst)
	for i := uint(0); len(available) > 0; i++ {
		current := available[0]
		current.CourseIndex = i
		available[0] = nil
		available = available[1:]
		dst = append(dst, current)
		for _, adj := range adjList[current] {
			inDegree[adj]--
			if inDegree[adj] == 0 {
				available = append(available, adj)
			}
		}
	}
	return dst
}

func (s *Server) CoursesPost(w http.ResponseWriter, r *http.Request) {
	submittedSchedule := scheduleDto{}
	parseBodyErr := json.NewDecoder(r.Body).Decode(&submittedSchedule)
	if parseBodyErr != nil {
		fmt.Println(parseBodyErr)
		respondWithError(w, "Something went wrong!", 400)
		return
	}

	courseMap, uuidToPrereqs := buildMaps(&submittedSchedule)

	for _, c := range submittedSchedule.Courses {
		course := courseMap[c.Uuid]
		for _, p := range c.Prerequisites {
			course.Prerequisites = append(course.Prerequisites, courseMap[p])
		}
	}

	adjList, available, inDegree := initializeGraph(&submittedSchedule, courseMap)

	sortedCourses := make([]*model.Course, 0, len(submittedSchedule.Courses))
	sortedCourses = sortCourses(adjList, available, inDegree, sortedCourses)
	if len(sortedCourses) != len(submittedSchedule.Courses) {
		respondWithError(w, "Prerequisite cycle detected!", 400)
		return
	}

	res := scheduleDto{}
	res.fromCourses(sortedCourses, uuidToPrereqs)

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(209)
	json.NewEncoder(w).Encode(res)
}
