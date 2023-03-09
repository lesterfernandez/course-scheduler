package me.lesterfernandez.CourseScheduler.schedule;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import me.lesterfernandez.CourseScheduler.course.Course;
import me.lesterfernandez.CourseScheduler.user.UserEntity;

@Entity
public class Schedule {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  @Getter
  @Setter
  private int workload;

  @NonNull
  @Getter
  @Setter
  @OneToMany(cascade = CascadeType.ALL)
  private List<Course> courses;

  @NonNull
  @Getter
  @Setter
  @OneToOne(cascade = CascadeType.PERSIST)
  @JsonIgnore
  private UserEntity user;

  public Schedule(int workload, List<Course> courses, UserEntity user) {
    this.workload = workload;
    this.courses = courses;
    this.user = user;
  }

  protected Schedule() {}

  public Schedule(int workload, List<Course> courses) {
    this.workload = workload;
    this.courses = courses;
  }
}
