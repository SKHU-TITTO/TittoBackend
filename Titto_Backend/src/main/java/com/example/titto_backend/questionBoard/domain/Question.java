package com.example.titto_backend.questionBoard.domain;

import com.example.titto_backend.auth.domain.User;
import com.example.titto_backend.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Question extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "question_id")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "author")
  private User author;

  @Column(name = "status")
  private Status status;

  @Enumerated(EnumType.STRING)
  @Column(name = "department")
  private Department department;

  @Column(name = "question_title", nullable = false)
  private String title;

  @Column(name = "question_content", nullable = false, columnDefinition = "TEXT")
  private String content;

  public void update(String title, String content, Department department, Status status) {
    this.title = title;
    this.content = content;
    this.department = department;
    this.status = status;
  }

  //TODO: 이미지, 조회수, 댓글은 나중에 추가
//  @Column(name = "image_url")
//  private String imageUrl;
//
//  @Column(name = "view")
//  private int view;
//
//  @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
//  private List<Answer> answerList;

}
