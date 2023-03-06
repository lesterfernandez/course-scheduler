package me.lesterfernandez.CourseScheduler.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

  public UserEntity findByUsername(String username);

  public Boolean existsByUsername(String username);
}
