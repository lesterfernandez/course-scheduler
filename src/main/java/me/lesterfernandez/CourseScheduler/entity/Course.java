package me.lesterfernandez.CourseScheduler.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
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

  public Course(String letters, String number, int courseIndex) {
    this.letters = letters;
    this.number = number;
    this.courseIndex = courseIndex;
  }

  public Course(String letters, String number) {
    this.letters = letters;
    this.number = number;
  }

  protected Course() {
  }
}
