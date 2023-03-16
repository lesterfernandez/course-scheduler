package me.lesterfernandez.CourseScheduler.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import me.lesterfernandez.CourseScheduler.auth.AuthContext;
import me.lesterfernandez.CourseScheduler.user.UserEntity;
import me.lesterfernandez.CourseScheduler.user.UserService;

@RestController
@RequestMapping("/api/schedule")
public class ScheduleController {

  @Autowired
  private AuthContext authContext;
  @Autowired
  private ScheduleService scheduleService;
  @Autowired
  private UserService userService;

  @GetMapping
  public ResponseEntity<Schedule> getUserSchedule() {
    String username = authContext.getUsername();
    Schedule userSchedule = scheduleService.getUserSchedule(username);
    return new ResponseEntity<>(userSchedule, HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<Schedule> setUserSchedule(@RequestBody Schedule schedule) {
    UserEntity user = userService.findByUsername(authContext.getUsername());
    scheduleService.setUserSchedule(schedule, user);
    return new ResponseEntity<>(schedule, HttpStatus.CREATED);
  }

}
