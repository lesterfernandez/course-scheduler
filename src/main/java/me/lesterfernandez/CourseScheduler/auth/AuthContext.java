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

  public boolean authorized;
  public ResponseEntity<String> authorizationFailedResponse = new ResponseEntity<>("Unauthorized",
      HttpStatus.UNAUTHORIZED);
  @Autowired
  private JwtComponent jwtComponent;
  @Getter
  @Setter
  private String username;
  @Getter
  @Setter
  private String token;

  public boolean authorize(HttpServletRequest request, HttpServletResponse response) {
    if (request.getCookies() == null) {
      return false;
    }

    Optional<Cookie> jwtOpt = Arrays.stream(request.getCookies())
        .filter(cookie -> "jwt".equals(cookie.getName()))
        .findAny();

    if (jwtOpt.isEmpty()) {
      return false;
    }

    String token = jwtOpt.get().getValue();
    authorized = jwtComponent.validateToken(token);
    this.username = jwtComponent.getUsernameFromToken(token);
    this.token = token;
    return authorized;
  }
}
