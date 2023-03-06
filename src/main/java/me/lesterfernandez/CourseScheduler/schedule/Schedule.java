package me.lesterfernandez.CourseScheduler.schedule;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import me.lesterfernandez.CourseScheduler.course.Course;

@Entity
@AllArgsConstructor
public class Schedule {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  @NonNull
  @Getter
  @Setter
  private int workload;

  @NonNull
  @Getter
  @Setter
  @OneToMany(cascade = CascadeType.ALL)
  private List<Course> courses;

  public Schedule(int workload, List<Course> courses) {
    this.workload = workload;
    this.courses = courses;
  }

  protected Schedule() {
  }
}
