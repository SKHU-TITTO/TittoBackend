package com.example.titto_backend.matchingBoard.dto.response.matchingPostReviewResponse;

import com.example.titto_backend.matchingBoard.domain.review.MatchingPostReview;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
public class MatchingPostReviewResponseDto {
    private Long reviewId;
    private String reviewAuthor;
    private String content;
    private LocalDateTime updateDate;

    public MatchingPostReviewResponseDto(MatchingPostReview matchingPostReview) {
        this.reviewId = matchingPostReview.getReview_id();
        this.reviewAuthor = matchingPostReview.getReviewAuthor().getNickname();
        this.content = matchingPostReview.getContent();
        this.updateDate = matchingPostReview.getUpdateDate();
    }
}
