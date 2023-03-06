package me.lesterfernandez.CourseScheduler.schedule;

import java.util.Optional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/schedules")
public class ScheduleController {

  private final ScheduleService scheduleService;

  public ScheduleController(ScheduleService scheduleService) {
    this.scheduleService = scheduleService;
  }

  @GetMapping("/{scheduleId}")
  private Schedule getUserSchedule(@PathVariable long scheduleId) {
    Optional<Schedule> userSchedule = scheduleService.getUserSchedule(scheduleId);
    return userSchedule.orElse(null);
  }

  @PostMapping
  private void setUserSchedule(@RequestBody Schedule schedule) {
    scheduleService.setUserSchedule(schedule);
  }

}
