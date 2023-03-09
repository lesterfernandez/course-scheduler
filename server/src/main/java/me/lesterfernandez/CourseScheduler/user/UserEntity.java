package me.lesterfernandez.CourseScheduler.user;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

  public UserEntity(String username, String password) {
    this.username = username;
    this.password = password;
  }
}
