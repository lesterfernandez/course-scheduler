package me.lesterfernandez.CourseScheduler.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.NoArgsConstructor;

@Service
@NoArgsConstructor
public class UserService {

  @Autowired
  private UserRepository userRepository;

  public UserEntity findByUsername(String username) {
    return userRepository.findByUsername(username);
  }

  public boolean existsByUsername(String username) {
    return userRepository.existsByUsername(username);
  }

  public void save(UserEntity user) {
    userRepository.save(user);
  }

}
