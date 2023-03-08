package me.lesterfernandez.CourseScheduler.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Optional;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class AuthContext {

  public static final ResponseEntity<String> authorizationFailedResponse = new ResponseEntity<>(
      "Unauthorized",
      HttpStatus.UNAUTHORIZED);
  public boolean authorized;
  @Autowired
  private JwtComponent jwtComponent;
  @Getter
  @Setter
  private String username;
  @Getter
  @Setter
  private String token;

  public void authorize(HttpServletRequest request, HttpServletResponse response) {
    if (request.getCookies() == null) {
      this.authorized = false;
      return;
    }

    Optional<Cookie> jwtOpt = Arrays.stream(request.getCookies())
        .filter(cookie -> "jwt".equals(cookie.getName()))
        .findAny();

    if (jwtOpt.isEmpty()) {
      this.authorized = false;
      return;
    }

    String token = jwtOpt.get().getValue();
    authorized = jwtComponent.validateToken(token);
    this.username = jwtComponent.getUsernameFromToken(token);
    this.token = token;
    this.authorized = true;
  }
}
