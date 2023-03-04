package me.lesterfernandez.CourseScheduler.repository;

import me.lesterfernandez.CourseScheduler.entity.Course;
import me.lesterfernandez.CourseScheduler.entity.Schedule;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends CrudRepository<Course, Long> {

}
