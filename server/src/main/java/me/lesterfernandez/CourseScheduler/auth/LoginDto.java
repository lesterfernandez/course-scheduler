package me.lesterfernandez.CourseScheduler.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginDto {
  private String username;
  private String password;
}
