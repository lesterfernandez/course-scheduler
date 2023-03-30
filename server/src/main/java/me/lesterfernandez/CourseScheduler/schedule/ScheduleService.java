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

  public ScheduleDto getUserSchedule(String username) {
    Schedule schedule = scheduleRepository.findByUserUsername(username);
    if (schedule == null) {
      return new ScheduleDto();
    }
    return new ScheduleDto(schedule);
  }

  public void setUserSchedule(Schedule schedule, UserEntity user) {
    if (user.getSchedule() != null) {
      schedule.setId(user.getSchedule().getId());
      scheduleRepository.delete(user.getSchedule());
    }
    orderCourses(schedule);
    user.setSchedule(schedule);
    schedule.setUser(user);
    userRepository.save(user);
  }

  private void orderCourses(Schedule userSchedule) {
    List<Course> courses = userSchedule.getCourses();
    for (int i = 0; i < courses.size(); i++) {
      Course course = courses.get(i);
      course.setCourseIndex(i);
    }
    courses.sort((a, b) -> a.getCourseIndex() - b.getCourseIndex());
  }
}
