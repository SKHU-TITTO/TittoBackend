package com.example.titto_backend.matchingBoard.domain.matchingBoard;

//import com.example.titto_backend.converter.CategoryToIntegerConverter;

import com.example.titto_backend.auth.domain.User;
import com.example.titto_backend.common.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
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
public class MatchingPost extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "matchingPost_id")
    private Long matchingPostId;

    // 직상자
    @ManyToOne
    @JoinColumn(name = "post_author")
    private User user;

    // 매칭게시판
    @OneToOne(mappedBy = "matchingPost", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private MatchingBoard matchingBoard;

    // 카테고리(멘토, 멘티, 한솥밥, 스터디)
//    @Convert(converter = CategoryToIntegerConverter.class)

    @Enumerated(EnumType.STRING)
    private Category category;

    // 상태(모집중, 모집 완료)
    @Enumerated(EnumType.STRING)
    private Status status;

    // 제목
    @Column(name = "title", nullable = false)
    private String title;

    //내용
    @Lob
    @Column(name = "content", columnDefinition = "LONGTEXT", nullable = false)
    private String content;

    // 조회수
    @Setter
    @Column(name = "view_count", columnDefinition = "integer default 0")
    private Integer viewCount;

    // 댓글 수
    @Column(name = "review_count", columnDefinition = "integer default 0")
    private Integer review_count;

    public void updateViewCount() {
        this.viewCount++;
    }

    @PrePersist
    public void prePersist() {
        this.viewCount = this.viewCount == null ? 0 : this.viewCount;
        this.review_count = this.review_count == null ? 0 : this.review_count;
    }

    public void update(Category category, String title, String content, Status status) {
        this.category = category;
        this.title = title;
        this.content = content;
        this.status = status;
    }
}
