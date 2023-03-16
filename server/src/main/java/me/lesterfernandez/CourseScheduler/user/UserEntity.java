package me.lesterfernandez.CourseScheduler.user;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.lesterfernandez.CourseScheduler.schedule.Schedule;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEntity {

  @Id
  @GeneratedValue
  private Long id;

  @Getter
  @Setter
  private String username;

  @Getter
  @Setter
  private String password;

  @Getter
  @Setter
  @OneToOne(cascade = CascadeType.ALL)
  private Schedule schedule;

  public UserEntity(String username, String password) {
    this.username = username;
    this.password = password;
  }
}
