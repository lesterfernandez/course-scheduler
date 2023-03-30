package me.lesterfernandez.CourseScheduler.course;

import java.util.ArrayList;
import java.util.List;
import jakarta.annotation.Nonnull;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
  @Nonnull
  private String uuid;

  @Getter
  @Setter
  @Nonnull
  private String letters;

  @Getter
  @Setter
  @Nonnull
  private String number;

  @Getter
  @Setter
  private int courseIndex;

  @Getter
  @Setter
  @Enumerated(EnumType.STRING)
  private Status status = Status.AVAILABLE;

  @Getter
  @Setter
  @OneToMany(cascade = CascadeType.ALL)
  private List<Course> prerequisites = new ArrayList<>();

  public Course(String uuid, String letters, String number, int courseIndex) {
    this.uuid = uuid;
    this.letters = letters;
    this.number = number;
    this.courseIndex = courseIndex;
  }

  public Course(String uuid, String letters, String number) {
    this.uuid = uuid;
    this.letters = letters;
    this.number = number;
  }

  public Course() {}
}
