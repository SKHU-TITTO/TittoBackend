package com.example.titto_backend.questionBoard.domain;

import com.example.titto_backend.auth.domain.User;
import com.example.titto_backend.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

//@Entity
//@Getter
//@Setter
//public class Answer extends BaseEntity {
//  @Id
//  @GeneratedValue(strategy = GenerationType.IDENTITY)
//  private Long id;
//
//  @JoinColumn(name = "author")
//  @ManyToOne
//  private User author;
//
//  @Column(name = "answer_content", nullable = false, columnDefinition = "TEXT")
//  private String content;
//
//  @JoinColumn(name = "question_id")
//  @ManyToOne
//  private Question question;
//
//  //채택 여부
//  @Column(name = "is_adopted")
//  private boolean isAdopted;
//}
