package com.example.titto_backend.dto.response.matchingPostResponse;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MatchingPostDeleteResponseDto {
    Long MatchingPostId;

    public static MatchingPostDeleteResponseDto of(Long MatchingPostId) {
        return new MatchingPostDeleteResponseDto(MatchingPostId);
    }
}
