package me.lesterfernandez.CourseScheduler.course;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;
import me.lesterfernandez.CourseScheduler.course.Course.Status;

@Getter
@Setter
public class CourseDto {
  private String uuid;

  private String letters;

  private String number;

  private int courseIndex;

  @Enumerated(EnumType.STRING)
  private Status status = Status.AVAILABLE;

  private List<String> prerequisites = new ArrayList<>();

  public CourseDto(Course course) {
    this.uuid = course.getUuid();
    this.letters = course.getLetters();
    this.number = course.getNumber();
    this.status = course.getStatus();
    this.prerequisites = course.getPrerequisites().stream().map(prereq -> prereq.getUuid()).toList();
  }
}
