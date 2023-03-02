package me.lesterfernandez.CourseScheduler;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

  @GetMapping("/")
  public String respond() {
    return "<h1>test</h1>";
  }
}
