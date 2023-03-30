package me.lesterfernandez.CourseScheduler.schedule;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.lesterfernandez.CourseScheduler.course.CourseDto;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleDto {
  private List<CourseDto> courses = new ArrayList<>();

  public ScheduleDto(Schedule schedule) {
    this.courses = schedule.getCourses().stream().map(CourseDto::new).toList();
  }
}
