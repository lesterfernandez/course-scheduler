package me.lesterfernandez.CourseScheduler.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;

@Component
public class AuthContext {

  public static final ResponseEntity<String> authorizationFailedResponse =
      new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);

  public boolean authorized;

  @Autowired
  private JwtComponent jwtComponent;

  @Getter
  @Setter
  private String username;

  @Getter
  @Setter
  private String token;

  public void authorize(HttpServletRequest request) {
    String tokenHeader = request.getHeader("authorization");
    if (tokenHeader == null || !tokenHeader.startsWith("Bearer")) {
      this.authorized = false;
      return;
    }
    String token = tokenHeader.split("Bearer ")[1];
    this.authorized = jwtComponent.validateToken(token);
    this.username = jwtComponent.getUsernameFromToken(token);
    this.token = token;
    this.authorized = true;
  }
}
