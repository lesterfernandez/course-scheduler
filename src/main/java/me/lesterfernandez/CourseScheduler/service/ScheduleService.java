package me.lesterfernandez.CourseScheduler.service;

import java.util.List;
import java.util.Optional;
import me.lesterfernandez.CourseScheduler.entity.Course;
import me.lesterfernandez.CourseScheduler.entity.Schedule;
import me.lesterfernandez.CourseScheduler.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScheduleService {

  @Autowired
  private ScheduleRepository scheduleRepository;

  public Optional<Schedule> getUserSchedule(long scheduleId) {
    return scheduleRepository.findById(scheduleId);
  }

  public void setUserSchedule(Schedule userSchedule) {
    orderCourses(userSchedule);
    scheduleRepository.save(userSchedule);
  }

  private void orderCourses(Schedule userSchedule) {
    List<Course> courses = userSchedule.getCourses();
    for (int i = 0; i < courses.size(); i++) {
      Course course = courses.get(i);
      course.setCourseIndex(i);
    }
  }

}
