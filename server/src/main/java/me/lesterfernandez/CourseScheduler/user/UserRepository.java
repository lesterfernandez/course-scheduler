package me.lesterfernandez.CourseScheduler.user;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {

  public UserEntity findByUsername(String username);

  public boolean existsByUsername(String username);
}
