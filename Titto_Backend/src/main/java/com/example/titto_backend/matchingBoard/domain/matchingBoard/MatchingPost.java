package com.example.titto_backend.matchingBoard.domain.matchingBoard;

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
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
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

    @ManyToOne
    @Setter
    @JoinColumn(name = "author")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private Category category;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(name = "title", nullable = false)
    private String title;

    @Lob
    @Column(name = "content", columnDefinition = "LONGTEXT", nullable = false)
    private String content;

    @Setter
    @Column(name = "view_count", columnDefinition = "integer default 0")
    private Integer viewCount;

    @Setter
    @Column(name = "review_count", columnDefinition = "integer default 0")
    private Integer reviewCount;

    @PrePersist
    public void prePersist() {
        this.viewCount = this.viewCount == null ? 0 : this.viewCount;
        this.reviewCount = this.reviewCount == null ? 0 : this.reviewCount;
    }

    public void update(Category category, String title, String content, Status status) {
        this.category = category;
        this.title = title;
        this.content = content;
        this.status = status;
    }

    public void updateViewCount() {
        this.viewCount++;
    }

    public void increaseReviewCount() {
        this.reviewCount++;
    }

    public void decreaseReviewCount() {
        if (this.reviewCount != null && this.reviewCount > 0) {
            this.reviewCount--;
        }
    }

}
