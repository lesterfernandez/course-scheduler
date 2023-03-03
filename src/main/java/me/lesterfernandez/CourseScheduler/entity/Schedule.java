package me.lesterfernandez.CourseScheduler.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.List;

@Entity
public class Schedule {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  private String description;
  @OneToMany
  private List<Course> courses;

  public String getDescription() {
    return description;
  }

  public List<Course> getScheduleCourses() {
    return courses;
  }
}
