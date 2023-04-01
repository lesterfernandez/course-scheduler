package me.lesterfernandez.CourseScheduler.course;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.lesterfernandez.CourseScheduler.course.Course.Status;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseDto {
  private String uuid;

  private String letters;

  private String number;

  private int courseIndex;

  @Enumerated(EnumType.STRING)
  @Builder.Default()
  private Status status = Status.AVAILABLE;

  @Builder.Default()
  private List<String> prerequisites = new ArrayList<>();

  public CourseDto(Course course) {
    this.uuid = course.getUuid();
    this.letters = course.getLetters();
    this.number = course.getNumber();
    this.status = course.getStatus();
    this.courseIndex = course.getCourseIndex();
    this.prerequisites = course.getPrerequisites().stream().map(prereq -> prereq.getUuid()).toList();
  }
}
