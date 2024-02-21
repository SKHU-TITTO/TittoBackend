package com.example.titto_backend.matchingBoard.dto.response.matchingPostReviewResponse;

import com.example.titto_backend.matchingBoard.domain.review.MatchingPostReview;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchingPostReviewResponseDto {

    private Long reviewId;
    private String reviewAuthor;
    private String profile;
    private String content;
    private LocalDateTime updateDate;

    public MatchingPostReviewResponseDto(MatchingPostReview matchingPostReview) {
        this.reviewId = matchingPostReview.getReview_id();
        this.reviewAuthor = matchingPostReview.getReviewAuthor().getNickname();
        this.profile = matchingPostReview.getReviewAuthor().getProfile();
        this.content = matchingPostReview.getContent();
        this.updateDate = matchingPostReview.getUpdateDate();
    }
}
