package com.example.titto_backend.domain;

import com.example.titto_backend.domain.MatchingBoard.MatchingPost;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Review extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long review_id;

    // 댓글 작성자
    @ManyToOne
    @JoinColumn(name = "review_author")
    private User user;

    // 게시글
    @ManyToOne
    @JoinColumn(name = "MatchingPost_id")
    private MatchingPost matchingPost;

    // 내용
    @Column(name = "review_content",nullable = false)
    private String content;

}
