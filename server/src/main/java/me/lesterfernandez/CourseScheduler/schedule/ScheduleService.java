package me.lesterfernandez.CourseScheduler.schedule;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import me.lesterfernandez.CourseScheduler.course.Course;

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
