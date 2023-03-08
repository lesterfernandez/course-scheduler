package me.lesterfernandez.CourseScheduler.schedule;

import java.util.Optional;
import me.lesterfernandez.CourseScheduler.auth.AuthContext;
import me.lesterfernandez.CourseScheduler.user.UserEntity;
import me.lesterfernandez.CourseScheduler.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/schedules")
public class ScheduleController {

  @Autowired
  private AuthContext authContext;
  @Autowired
  private ScheduleService scheduleService;
  @Autowired
  private UserService userService;

  @GetMapping("/{scheduleId}")
  private ResponseEntity<?> getUserSchedule(@PathVariable long scheduleId) {
    Optional<Schedule> userSchedule = scheduleService.getUserSchedule(scheduleId);
    return new ResponseEntity<>(userSchedule.orElse(null), HttpStatus.OK);
  }

  @PostMapping
  private ResponseEntity<?> setUserSchedule(@RequestBody Schedule schedule) {
    UserEntity user = userService.findByUsername(authContext.getUsername());
    if (user == null) {
      return AuthContext.authorizationFailedResponse;
    }
    schedule.setUser(user);
    scheduleService.setUserSchedule(schedule);
    Schedule response = new Schedule(schedule.getWorkload(), schedule.getCourses());
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

}
