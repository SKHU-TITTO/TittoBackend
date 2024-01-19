package com.example.titto_backend.dto.response.matchingPostReviewResponse;

import com.example.titto_backend.domain.review.MatchingPostReview;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MatchingPostReviewUpdateResponseDto {
    private Long reviewId;
    private String reviewAuthor;
    private String content;
    private LocalDateTime updateDate;

    public MatchingPostReviewUpdateResponseDto(MatchingPostReview matchingPostReview) {
        this.reviewId = matchingPostReview.getReview_id();
        this.reviewAuthor = matchingPostReview.getReviewAuthor().getNickname();
        this.content = matchingPostReview.getContent();
        this.updateDate = matchingPostReview.getUpdateDate();
    }
}
