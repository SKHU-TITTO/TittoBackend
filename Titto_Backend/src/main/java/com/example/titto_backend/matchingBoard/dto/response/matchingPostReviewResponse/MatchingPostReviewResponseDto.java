package com.example.titto_backend.matchingBoard.dto.response.matchingPostReviewResponse;

import com.example.titto_backend.matchingBoard.domain.review.MatchingPostReview;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchingPostReviewResponseDto {

    private Long reviewAuthorId;
    private Long reviewId;
    private String reviewAuthor;
    private String profile;
    private String content;
    private LocalDateTime updateDate;
    private Integer level;

    public MatchingPostReviewResponseDto(MatchingPostReview matchingPostReview) {
        this.reviewAuthorId = matchingPostReview.getReviewAuthor().getId();
        this.reviewId = matchingPostReview.getReview_id();
        this.reviewAuthor = matchingPostReview.getReviewAuthor().getNickname();
        this.profile = matchingPostReview.getReviewAuthor().getProfile();
        this.content = matchingPostReview.getContent();
        this.updateDate = matchingPostReview.getUpdateDate();
        this.level = matchingPostReview.getReviewAuthor().getLevel();
    }

}
