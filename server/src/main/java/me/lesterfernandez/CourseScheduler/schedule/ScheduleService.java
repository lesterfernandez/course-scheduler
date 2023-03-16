package me.lesterfernandez.CourseScheduler.schedule;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import me.lesterfernandez.CourseScheduler.course.Course;
import me.lesterfernandez.CourseScheduler.user.UserEntity;
import me.lesterfernandez.CourseScheduler.user.UserRepository;

@Service
public class ScheduleService {

  @Autowired
  private ScheduleRepository scheduleRepository;

  @Autowired
  private UserRepository userRepository;

  public Optional<Schedule> getUserSchedule(long scheduleId) {
    return scheduleRepository.findById(scheduleId);
  }

  public Schedule getUserSchedule(String username) {
    return scheduleRepository.findByUserUsername(username);
  }

  public void setUserSchedule(Schedule schedule, UserEntity user) {
    schedule.setUser(user);
    user.setSchedule(schedule);
    orderCourses(schedule);
    userRepository.save(user);
    scheduleRepository.save(schedule);
  }

  private void orderCourses(Schedule userSchedule) {
    List<Course> courses = userSchedule.getCourses();
    for (int i = 0; i < courses.size(); i++) {
      Course course = courses.get(i);
      course.setCourseIndex(i);
    }
  }

}
