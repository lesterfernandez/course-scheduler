package me.lesterfernandez.CourseScheduler.auth;

import at.favre.lib.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncoder {
  public String encode(String password) {
    return BCrypt.withDefaults().hashToString(6, password.toCharArray());
  }
}
