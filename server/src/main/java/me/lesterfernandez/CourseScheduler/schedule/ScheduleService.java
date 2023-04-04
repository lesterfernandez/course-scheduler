package me.lesterfernandez.CourseScheduler.schedule;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.lesterfernandez.CourseScheduler.course.Course;
import me.lesterfernandez.CourseScheduler.course.CourseDto;
import me.lesterfernandez.CourseScheduler.user.UserEntity;
import me.lesterfernandez.CourseScheduler.user.UserService;

@Service
public class ScheduleService {

  @Autowired
  private ScheduleRepository scheduleRepository;

  @Autowired
  private UserService userService;

  public Optional<Schedule> getUserSchedule(long scheduleId) {
    return scheduleRepository.findById(scheduleId);
  }

  public ScheduleDto getUserSchedule(String username) {
    Schedule schedule = scheduleRepository.findByUserUsername(username);
    if (schedule == null) {
      return new ScheduleDto();
    }
    schedule.getCourses().sort((a, b) -> a.getCourseIndex() - b.getCourseIndex());
    return new ScheduleDto(schedule);
  }

  public ScheduleDto setUserSchedule(ScheduleDto scheduleDto, UserEntity user) {
    Schedule schedule = new Schedule();
    List<Course> courses = schedule.getCourses();
    List<CourseDto> courseDtos = scheduleDto.getCourses();
    Map<String, CourseDto> courseDtoMap = new HashMap<>();
    Map<String, Course> courseMap = new HashMap<>();

    courseDtos.forEach(courseDto -> {
      courseDtoMap.put(courseDto.getUuid(), courseDto);
      Course course = new Course(
          courseDto.getUuid(),
          courseDto.getLetters(),
          courseDto.getNumber(),
          courseDto.getCourseIndex(),
          courseDto.getStatus());
      courseMap.put(course.getUuid(), course);
      courses.add(course);
    });

    courses.forEach(course -> {
      CourseDto courseDto = courseDtoMap.get(course.getUuid());
      courseDto.getPrerequisites().forEach(prereqUuid -> {
        course.getPrerequisites().add(courseMap.get(prereqUuid));
      });
    });

    orderCourses(schedule);

    if (user.getSchedule() != null) {
      schedule.setId(user.getSchedule().getId());
      scheduleRepository.delete(user.getSchedule());
    }

    user.setSchedule(schedule);
    schedule.setUser(user);
    userService.save(user);

    return new ScheduleDto(schedule);
  }

  private void orderCourses(Schedule userSchedule) throws IllegalArgumentException {
    List<Course> nodes = userSchedule.getCourses();

    DiGraph<Course> scheduleGraph = new DiGraph<>(nodes);
    List<Course> sortedCourses = new ArrayList<>();
    Queue<Course> available = new ArrayDeque<>();
    Map<Course, Integer> inDegreeMap = new HashMap<>();

    nodes.forEach(node -> {
      int inDegree = 0;
      for (Course prereq : node.getPrerequisites()) {
        scheduleGraph.addEdge(prereq, node);
        inDegree++;
      }
      inDegreeMap.put(node, inDegree);
      if (inDegree == 0) {
        available.add(node);
      }
    });

    // A variant of Topological Sort
    while (!available.isEmpty()) {
      Course current = available.remove();
      sortedCourses.add(current);
      List<Course> neighbors = scheduleGraph.getNeighbors(current);
      neighbors.forEach(neighbor -> {
        int inDegree = inDegreeMap.get(neighbor) - 1;
        inDegreeMap.put(neighbor, inDegree);
        if (inDegree == 0) {
          available.add(neighbor);
        }
      });
    }

    if (sortedCourses.size() != nodes.size()) {
      throw new IllegalArgumentException("Prerequisite cycle detected!");
    }

    for (int i = 0; i < sortedCourses.size(); i++) {
      System.out.println(sortedCourses.get(i));
      sortedCourses.get(i).setCourseIndex(i);
    }
    userSchedule.setCourses(sortedCourses);
  }

  /***
   * A very rudimentary Directed Graph.
   * It has only the minimum functionality for my use case.
   * In reality, a lot more could be added here.
   **/
  private static class DiGraph<T> {
    private Map<T, List<T>> graph = new HashMap<>();

    public DiGraph(List<T> nodes) {
      for (T node : nodes) {
        graph.put(node, new ArrayList<>());
      }
    }

    public void addEdge(T from, T to) {
      List<T> adjacent = graph.getOrDefault(from, new ArrayList<>());
      adjacent.add(to);
      graph.put(from, adjacent);
    }

    public List<T> getNeighbors(T node) {
      return graph.get(node);
    }
  }
}
