package com.example.titto_backend.matchingBoard.domain.review;

import com.example.titto_backend.auth.domain.User;
import com.example.titto_backend.common.BaseEntity;
import com.example.titto_backend.matchingBoard.domain.matchingBoard.MatchingPost;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchingPostReview extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long review_id;

    // 댓글 작성자
    @ManyToOne
    @Setter
    @JoinColumn(name = "review_author")
    private User reviewAuthor;

    // 게시글
    @ManyToOne
    @JoinColumn(name = "matchingPost_id")
    private MatchingPost matchingPost;

    // 내용
    @Column(name = "review_content", nullable = false)
    private String content;

}
