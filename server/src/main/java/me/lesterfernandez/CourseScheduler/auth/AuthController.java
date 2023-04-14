package me.lesterfernandez.CourseScheduler.auth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
import lombok.NoArgsConstructor;
import me.lesterfernandez.CourseScheduler.course.CourseDto;
import me.lesterfernandez.CourseScheduler.schedule.ScheduleDto;
import me.lesterfernandez.CourseScheduler.schedule.ScheduleService;
import me.lesterfernandez.CourseScheduler.user.UserEntity;
import me.lesterfernandez.CourseScheduler.user.UserService;
import me.lesterfernandez.CourseScheduler.utils.ErrorResponseDto;

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
        return new ResponseEntity<>(new ErrorResponseDto("Username taken"), HttpStatus.CONFLICT);
      }

      String username = loginDto.getUsername();
      String password = passwordEncoder.encode(loginDto.getPassword());
      UserEntity user = new UserEntity(username, password);
      userService.save(user);
      String token = jwtComponent.generateToken(username);

      RegisterResponseDto registerResponse = new RegisterResponseDto(true, loginDto.getUsername(), token);
      return ResponseEntity.ok().body(registerResponse);
    } catch (Exception e) {
      System.out.println(e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
      return new ResponseEntity<>(
          new ErrorResponseDto("Something went wrong!"), HttpStatus.INTERNAL_SERVER_ERROR);
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
      ScheduleDto schedule = scheduleService.getUserSchedule(username);

      LoginResponseDto loginResponse = new LoginResponseDto(
          true, authContext.getUsername(), authContext.getToken(), schedule.getCourses());

      return ResponseEntity.ok().body(loginResponse);
    } catch (Exception e) {
      System.out.println(e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
      return new ResponseEntity<>(
          new ErrorResponseDto("Something went wrong!"), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
    try {
      UserEntity user = userService.findByUsername(loginDto.getUsername());

      if (user == null) {
        return new ResponseEntity<>(new ErrorResponseDto("Invalid Username or Password"), HttpStatus.UNAUTHORIZED);
      }

      boolean verified = passwordEncoder.verify(loginDto.getPassword(), user.getPassword());

      if (!verified) {
        return AuthContext.authorizationFailedResponse;
      }

      String token = jwtComponent.generateToken(user);

      List<CourseDto> userCourses = user.getSchedule() == null
          ? new ArrayList<>()
          : new ScheduleDto(user.getSchedule()).getCourses();

      LoginResponseDto response = new LoginResponseDto(
          true,
          user.getUsername(),
          token,
          userCourses);

      return ResponseEntity.ok().body(response);
    } catch (Exception e) {
      System.out.println(e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
      return new ResponseEntity<>(
          new ErrorResponseDto("Something went wrong!"), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class LoginDto {
    private String username;
    private String password;
  }

  @Data
  @AllArgsConstructor
  private static class LoginResponseDto {
    private boolean loggedIn;
    private String username;
    private String token;
    private List<CourseDto> courses;
  }

  @Data
  @AllArgsConstructor
  private static class RegisterResponseDto {
    private boolean loggedIn;
    private String username;
    private String token;
  }
}
