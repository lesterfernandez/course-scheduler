package me.lesterfernandez.CourseScheduler.course;

import java.util.List;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
public class Course {

  public enum Status {
    IN_PROGRESS, AVAILABLE,
  }

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Getter
  @Setter
  private String name;

  @Getter
  @Setter
  private int courseIndex;

  @Getter
  @Setter
  @Enumerated(EnumType.STRING)
  private Status status = Status.AVAILABLE;

  @Getter
  @Setter
  private List<String> prerequisites;

  public Course(String name, int courseIndex, Status status) {
    this.name = name;
    this.courseIndex = courseIndex;
    this.status = status;
  }

  public Course(String name) {
    this.name = name;
  }

  public Course() {}
}
