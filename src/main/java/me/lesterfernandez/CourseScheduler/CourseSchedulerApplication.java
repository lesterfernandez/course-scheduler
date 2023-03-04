package me.lesterfernandez.CourseScheduler;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CourseSchedulerApplication implements CommandLineRunner {

  public static void main(String[] args) {
    SpringApplication.run(CourseSchedulerApplication.class, args);
  }

//  @Autowired
//  private ScheduleService scheduleService;

  @Override
  public void run(String... args) throws Exception {
//    List<Course> courses = Arrays.asList(new Course("COP", "2800"), new Course("CLP", "1006"));
//    scheduleService.setUserSchedule(5, courses);
  }
}
