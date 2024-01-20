package com.example.titto_backend.dto.response.matchingPostReviewResponse;

import com.example.titto_backend.dto.response.matchingPostResponse.MatchingPostDeleteResponseDto;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MatchingPostReviewDeleteResponseDto {
    private Long reviewId;

    public static MatchingPostReviewDeleteResponseDto of(Long reviewId){
        return new MatchingPostReviewDeleteResponseDto(reviewId);
    }
}
