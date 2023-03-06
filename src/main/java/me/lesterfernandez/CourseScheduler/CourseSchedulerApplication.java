package me.lesterfernandez.CourseScheduler;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CourseSchedulerApplication implements CommandLineRunner {

  public static void main(String[] args) {
    SpringApplication.run(CourseSchedulerApplication.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
  }
}
