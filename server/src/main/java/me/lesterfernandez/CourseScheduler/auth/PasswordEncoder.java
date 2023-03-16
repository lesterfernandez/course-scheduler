package me.lesterfernandez.CourseScheduler.auth;

import org.springframework.stereotype.Component;
import at.favre.lib.crypto.bcrypt.BCrypt;

@Component
public class PasswordEncoder {
  public String encode(String password) {
    return BCrypt.withDefaults().hashToString(6, password.toCharArray());
  }

  public boolean verify(String password, String hashedPassword) {
    BCrypt.Result result = BCrypt.verifyer().verify(password.getBytes(), hashedPassword.getBytes());
    return result.verified;
  }

}
