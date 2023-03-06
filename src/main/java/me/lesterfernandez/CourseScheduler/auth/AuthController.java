package me.lesterfernandez.CourseScheduler.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  @Autowired
  private AuthenticationManager authenticationManager;
  @Autowired
  private UserService userService;
  @Autowired
  private PasswordEncoder passwordEncoder;
  @Autowired
  private JwtService jwtService;

  @PostMapping("/register")
  public ResponseEntity<String> register(@RequestBody LoginDto loginDto) {
    if (userService.userExists(loginDto)) {
      return new ResponseEntity<>("Username taken", HttpStatus.CONFLICT);
    }

    String hashedPassword = passwordEncoder.encode(loginDto.getPassword());
    UserEntity user = new UserEntity(loginDto.getUsername(), hashedPassword);
    userService.save(user);

    String token = jwtService.generateJwt(user.getUsername());
    return new ResponseEntity<>(token, HttpStatus.OK);
  }

  @GetMapping("/login")
  public ResponseEntity<String> login(@RequestBody LoginDto loginDto) {
    try {
      UserDetails user = userService.loadUserByUsername(loginDto.getUsername());
      String loginPass = passwordEncoder.encode(loginDto.getPassword());
      System.out.println(user.getPassword() + " ... " + loginPass);
      if (loginPass.equals(user.getPassword())) {
        String token = jwtService.generateJwt(loginDto.getUsername());
        return new ResponseEntity<>(token, HttpStatus.OK);
      }
      return new ResponseEntity<>("Wrong username or password", HttpStatus.BAD_REQUEST);
    } catch (Exception e) {
      return new ResponseEntity<>("Wrong username or password", HttpStatus.BAD_REQUEST);
    }
  }
}
