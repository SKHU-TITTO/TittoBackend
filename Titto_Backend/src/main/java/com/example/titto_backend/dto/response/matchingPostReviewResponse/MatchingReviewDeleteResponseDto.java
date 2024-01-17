package com.example.titto_backend.dto.response.matchingPostReviewResponse;

import com.example.titto_backend.dto.response.matchingPostResponse.MatchingPostDeleteResponseDto;

public class MatchingReviewDeleteResponseDto {
    private Long reviewId;

    public static MatchingPostDeleteResponseDto of(Long reviewId){
        return new MatchingPostDeleteResponseDto(reviewId);
    }
}
