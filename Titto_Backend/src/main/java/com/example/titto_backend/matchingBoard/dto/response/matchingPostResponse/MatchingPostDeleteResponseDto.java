package com.example.titto_backend.matchingBoard.dto.response.matchingPostResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MatchingPostDeleteResponseDto {

    Long MatchingPostId;

    public static MatchingPostDeleteResponseDto of(Long MatchingPostId) {
        return new MatchingPostDeleteResponseDto(MatchingPostId);
    }
}
