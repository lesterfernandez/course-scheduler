package me.lesterfernandez.CourseScheduler.auth;

import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.AllArgsConstructor;
import lombok.Data;
import me.lesterfernandez.CourseScheduler.user.UserEntity;
import me.lesterfernandez.CourseScheduler.user.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  @Autowired
  private JwtComponent jwtComponent;
  @Autowired
  private UserService userService;
  @Autowired
  private PasswordEncoder passwordEncoder;

  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody LoginDto loginDto) {
    try {
      if (userService.existsByUsername(loginDto.getUsername())) {
        System.out.println("username taken!");
        throw new IllegalArgumentException("Username taken");
      }

      UserEntity user = new UserEntity(loginDto.getUsername(), loginDto.getPassword());
      user.setPassword(passwordEncoder.encode(user.getPassword()));
      userService.save(user);

      String token = jwtComponent.generateToken(user.getUsername());

      ResponseCookie cookie = ResponseCookie.from("jwt", token).domain("localhost").path("/")
          .maxAge(1000 * 60 * 60 * 24 * 14).build();

      return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
          .body(new AuthResultDto(true, loginDto.getUsername()));
    } catch (Exception e) {
      System.out.println(Arrays.toString(e.getStackTrace()));
      System.out.println(e.getMessage());

      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
    try {
      UserEntity user = userService.findByUsername(loginDto.getUsername());
      String token = jwtComponent.generateToken(user);

      ResponseCookie cookie = ResponseCookie.from("jwt", token).domain("localhost").path("/")
          .maxAge(1000 * 60 * 60 * 24 * 14).build();

      return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
          .body(new AuthResultDto(true, loginDto.getUsername()));
    } catch (Exception e) {
      System.out.println(Arrays.toString(e.getStackTrace()));
      System.out.println(e.getMessage());
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
  }

  @Data
  @AllArgsConstructor
  private class AuthResultDto {

    private boolean loggedIn;
    private String username;
  }
}
