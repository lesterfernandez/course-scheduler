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

  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody LoginDto loginDto) {
    try {
      if (userService.existsByUsername(loginDto.getUsername())) {
        System.out.println("username taken!");
        return new ResponseEntity<>(new InvalidRequestDto("Username taken"), HttpStatus.CONFLICT);
      }

      UserEntity user = new UserEntity(loginDto.getUsername(), loginDto.getPassword());
      user.setPassword(passwordEncoder.encode(user.getPassword()));
      userService.save(user);

      String token = jwtComponent.generateToken(user.getUsername());
      return ResponseEntity.ok().body(new AuthResultDto(true, loginDto.getUsername(), token));
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
      if (authContext.authorized && authContext.getUsername() != null) {
        return ResponseEntity.ok()
            .body(new AuthResultDto(true, authContext.getUsername(), authContext.getToken()));
      }
    } catch (Exception e) {
      System.out.println(Arrays.toString(e.getStackTrace()));
      System.out.println(e.getMessage());
      return ResponseEntity.internalServerError()
          .body(new InvalidRequestDto("Something went wrong!"));
    }
    return AuthContext.authorizationFailedResponse;
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
    try {
      UserEntity user = userService.findByUsername(loginDto.getUsername());
      String token = jwtComponent.generateToken(user);
      return ResponseEntity.ok().body(new AuthResultDto(true, loginDto.getUsername(), token));
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
  }

  @Data
  @AllArgsConstructor
  private class InvalidRequestDto {
    private String errorMessage;
  }
}
