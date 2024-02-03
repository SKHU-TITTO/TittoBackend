package com.example.titto_backend.matchingBoard.dto.response.matchingPostReviewResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GetAllMatchingPostReviewsResponseDto {

    private List<MatchingPostReviewResponseDto> matchingPostReviewResponseDtos;

    public static GetAllMatchingPostReviewsResponseDto of(List<MatchingPostReviewResponseDto> postReviewResponseDtos) {
        return new GetAllMatchingPostReviewsResponseDto(postReviewResponseDtos);
    }
}
