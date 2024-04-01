package com.example.titto_backend.matchingBoard.dto.response.matchingPostReviewResponse;

import com.example.titto_backend.matchingBoard.domain.review.MatchingPostReview;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MatchingPostReviewCreateResponseDto {

    private Long reviewId;
    private String reviewAuthor;
    private String content;
    private LocalDateTime createDate;

    public MatchingPostReviewCreateResponseDto(MatchingPostReview matchingPostReview) {
        this.reviewId = matchingPostReview.getReview_id();
        this.reviewAuthor = matchingPostReview.getReviewAuthor().getNickname();
        this.content = matchingPostReview.getContent();
        this.createDate = matchingPostReview.getCreateDate();
    }

}
