package me.lesterfernandez.CourseScheduler.schedule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.lesterfernandez.CourseScheduler.course.Course;
import me.lesterfernandez.CourseScheduler.course.CourseDto;
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

  public void setUserSchedule(ScheduleDto scheduleDto, UserEntity user) {
    Schedule schedule = new Schedule();
    List<Course> courses = schedule.getCourses();
    List<CourseDto> courseDtos = scheduleDto.getCourses();
    Map<String, CourseDto> courseDtoMap = new HashMap<>();
    Map<String, Course> courseMap = new HashMap<>();

    courseDtos.forEach(courseDto -> {
      courseDtoMap.put(courseDto.getUuid(), courseDto);
      Course course = new Course(
          courseDto.getUuid(),
          courseDto.getLetters(),
          courseDto.getNumber(),
          courseDto.getCourseIndex());
      courseMap.put(course.getUuid(), course);
      courses.add(course);
    });

    courses.forEach(course -> {
      CourseDto courseDto = courseDtoMap.get(course.getUuid());
      courseDto.getPrerequisites().forEach(prereqUuid -> {
        course.getPrerequisites().add(courseMap.get(prereqUuid));
      });
    });

    // always replace student schedule
    // it would be ideal to update without deleting
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
