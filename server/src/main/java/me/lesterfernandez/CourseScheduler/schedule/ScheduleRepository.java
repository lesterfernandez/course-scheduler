package me.lesterfernandez.CourseScheduler.schedule;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends CrudRepository<Schedule, Long> {
  public Schedule findByUserId(Long id);
}
