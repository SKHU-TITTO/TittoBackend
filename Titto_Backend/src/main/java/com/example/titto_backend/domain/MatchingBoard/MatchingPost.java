package com.example.titto_backend.domain.MatchingBoard;

import com.example.titto_backend.domain.BaseEntity;
import com.example.titto_backend.domain.User;
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
public class MatchingPost extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "matchingPost_id")
    private int matchingPostId;

    // 직상자
    @ManyToOne
    @JoinColumn(name = "post_author")
    private User user;

    // 카테고리(멘토, 멘티, 한솥밥, 스터디)
    @Enumerated(EnumType.ORDINAL)
    private Category category;

    // 상태(모집중, 모집 완료)
    @Enumerated(EnumType.ORDINAL)
    private Status status;

    // 제목
    @Column(name = "title", nullable = false)
    private String title;

    //내용
    @Lob
    @Column(name = "content",columnDefinition = "TEXT", nullable = false)
    private String content;

    // 조회수
    @Column(name = "view_count", nullable = false)
    private int viewCount;

}
