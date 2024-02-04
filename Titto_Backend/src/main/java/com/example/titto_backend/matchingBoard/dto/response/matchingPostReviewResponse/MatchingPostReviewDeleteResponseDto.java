package com.example.titto_backend.matchingBoard.dto.response.matchingPostReviewResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchingPostReviewDeleteResponseDto {
    private Long reviewId;

    public static MatchingPostReviewDeleteResponseDto of(Long reviewId){
        return new MatchingPostReviewDeleteResponseDto(reviewId);
    }
}
