package com.example.titto_backend.dto.request.matchingPostReviewRequest;

import com.example.titto_backend.domain.review.MatchingPostReview;
import jakarta.validation.constraints.NotNull;

public class MatchingPostReviewUpdateRequestDto {
    @NotNull
    private String content;

    public MatchingPostReview toEntity() {
        return MatchingPostReview.builder()
                .content(content)
                .build();
    }
}
