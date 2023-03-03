package me.lesterfernandez.CourseScheduler.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Course {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  private String letters;
  private String numbers;

  protected Course() {
  }
}
