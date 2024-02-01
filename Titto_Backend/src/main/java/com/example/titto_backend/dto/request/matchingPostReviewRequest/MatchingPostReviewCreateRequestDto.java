package com.example.titto_backend.dto.request.matchingPostReviewRequest;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MatchingPostReviewCreateRequestDto {
    @NotNull
    private Long postId;
    @NotNull
    private String content;

}
