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
import lombok.ToString;
import me.lesterfernandez.CourseScheduler.course.Course;
import me.lesterfernandez.CourseScheduler.user.UserEntity;

@Entity
@ToString
@Getter
@Setter
public class Schedule {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @JsonIgnore
  private Long id;

  @NonNull
  @OneToMany(cascade = CascadeType.ALL)
  private List<Course> courses;

  @NonNull
  @OneToOne(mappedBy = "schedule", cascade = CascadeType.ALL)
  @JsonIgnore
  private UserEntity user;

  public Schedule(int workload, List<Course> courses, UserEntity user) {
    this.courses = courses;
    this.user = user;
  }

  public Schedule() {}

  public Schedule(int workload, List<Course> courses) {
    this.courses = courses;
  }
}
