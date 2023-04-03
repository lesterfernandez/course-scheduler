package me.lesterfernandez.CourseScheduler.course;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.annotation.Nonnull;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Course {

  public static enum Status {
    AVAILABLE, COMPLETED
  }

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @JsonIgnore
  private Long id;

  @Nonnull
  @EqualsAndHashCode.Include
  private String uuid;

  @Nonnull
  private String letters;

  @Nonnull
  private String number;

  private int courseIndex;

  @Enumerated(EnumType.STRING)
  private Status status = Status.AVAILABLE;

  @ManyToMany(cascade = CascadeType.ALL)
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

  public String toString() {
    return "uuid=" + uuid + ", letters=" + letters + ", number=" + number;
  }
}
