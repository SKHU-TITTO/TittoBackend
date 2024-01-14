package com.example.titto_backend.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;

@Entity
@RequiredArgsConstructor
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String nickname;

  private String studentNumber;
  private String department;
  private String profile_image;
  private LocalDateTime createDate;
  private Level level;
  //post count;
  private Integer postCount;
  //badge
  private Badge badge;
  //available point
  private Integer availablePoint;

}
