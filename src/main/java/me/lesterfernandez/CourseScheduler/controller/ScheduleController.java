package me.lesterfernandez.CourseScheduler.controller;

import me.lesterfernandez.CourseScheduler.entity.Schedule;
import me.lesterfernandez.CourseScheduler.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/schedules")
public class ScheduleController {

  @Autowired
  private ScheduleService scheduleService;

  @GetMapping("{userId}")
  private String getUserSchedule(@PathVariable long userId) {
    return Long.toString(userId);
  }

  @PostMapping("")
  private String setUserSchedule(@RequestBody Schedule schedule) {
    return Integer.toString(schedule.getScheduleCourses().size());
  }

}
