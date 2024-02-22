package com.example.titto_backend.questionBoard.domain;

import com.example.titto_backend.auth.domain.User;
import com.example.titto_backend.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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

    @Column(name = "send_Experience", nullable = false)
    private Integer sendExperience;

    // 채택된 답변 ID
    @Setter
    @OneToOne
    @JoinColumn(name = "accepted_answer")
    private Answer acceptedAnswer;

    public void update(String title, String content, Department department, Status status) {
        this.title = title;
        this.content = content;
        this.department = department;
        this.status = status;
    }

// 조회수
//  @Column(name = "view")
//  private int view;

    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
    private List<Answer> answers = new ArrayList<>();
}
