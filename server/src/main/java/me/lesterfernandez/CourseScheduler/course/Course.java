package me.lesterfernandez.CourseScheduler.course;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Entity
public class Course {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @NonNull
  @Getter
  @Setter
  private String letters;

  @NonNull
  @Getter
  @Setter
  private String number;

  @Getter
  @Setter
  private int courseIndex;

  @Getter
  @Setter
  private boolean completed = false;

  public Course(String letters, String number, int courseIndex, boolean completed) {
    this.letters = letters;
    this.number = number;
    this.courseIndex = courseIndex;
    this.completed = completed;
  }

  public Course(String letters, String number) {
    this.letters = letters;
    this.number = number;
  }

  public Course() {}
}
