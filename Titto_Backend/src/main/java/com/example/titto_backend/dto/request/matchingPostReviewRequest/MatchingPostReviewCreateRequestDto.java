package com.example.titto_backend.dto.request.matchingPostReviewRequest;

import com.example.titto_backend.domain.review.MatchingPostReview;
import jakarta.validation.constraints.NotNull;

public class MatchingPostReviewCreateRequestDto {
    @NotNull
    private String content;

    public MatchingPostReview toEntity() {
        return MatchingPostReview.builder()
                .content(content)
                .build();
    }
}
