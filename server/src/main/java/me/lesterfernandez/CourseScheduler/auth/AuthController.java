package me.lesterfernandez.CourseScheduler.auth;

import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import me.lesterfernandez.CourseScheduler.schedule.Schedule;
import me.lesterfernandez.CourseScheduler.schedule.ScheduleService;
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

  @Autowired
  private AuthContext authContext;

  @Autowired
  private ScheduleService scheduleService;

  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody LoginDto loginDto) {
    try {
      if (userService.existsByUsername(loginDto.getUsername())) {
        System.out.println("username taken!");
        return new ResponseEntity<>(new InvalidRequestDto("Username taken"), HttpStatus.CONFLICT);
      }

      String username = loginDto.getUsername();
      String password = passwordEncoder.encode(loginDto.getPassword());
      UserEntity user = new UserEntity(username, password);
      userService.save(user);
      String token = jwtComponent.generateToken(username);

      return ResponseEntity.ok()
          .body(new AuthResultDto(true, loginDto.getUsername(), token, user.getSchedule()));
    } catch (Exception e) {
      System.out.println(Arrays.toString(e.getStackTrace()));
      System.out.println(e.getMessage());

      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
  }

  @GetMapping("/login")
  public ResponseEntity<?> implicitLogin(HttpServletRequest req) {
    try {
      authContext.authorize(req);
      if (!authContext.authorized || authContext.getUsername() == null) {
        return AuthContext.authorizationFailedResponse;
      }

      String username = authContext.getUsername();
      Schedule schedule = scheduleService.getUserSchedule(username);

      return ResponseEntity.ok().body(
          new AuthResultDto(true, authContext.getUsername(), authContext.getToken(), schedule));
    } catch (Exception e) {
      System.out.println(Arrays.toString(e.getStackTrace()));
      System.out.println(e.getMessage());
      return ResponseEntity.internalServerError()
          .body(new InvalidRequestDto("Something went wrong!"));
    }
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
    try {
      UserEntity user = userService.findByUsername(loginDto.getUsername());
      boolean verified = passwordEncoder.verify(loginDto.getPassword(), user.getPassword());
      if (!verified) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
      }
      String token = jwtComponent.generateToken(user);
      AuthResultDto response =
          new AuthResultDto(true, user.getUsername(), token, user.getSchedule());
      return ResponseEntity.ok().body(response);
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
    private String token;
    private Schedule schedule;
  }

  @Data
  @AllArgsConstructor
  private class InvalidRequestDto {
    private String errorMessage;
  }
}
