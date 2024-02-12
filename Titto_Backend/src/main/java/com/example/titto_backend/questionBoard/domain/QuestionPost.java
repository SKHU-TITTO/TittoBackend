package com.example.titto_backend.questionBoard.domain;

import com.example.titto_backend.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.RequiredArgsConstructor;

@Entity
@RequiredArgsConstructor
public class QuestionPost extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "questionPost_id")
  private Long id;

  @Enumerated(EnumType.STRING)
  @Column(name = "department")
  private Department department;

  @Column(name = "question_title", nullable = false)
  private String title;

  @Column(name = "question_content", nullable = false, columnDefinition = "TEXT")
  private String content;

  @Column(name = "image_url")
  private String imageUrl;

}
