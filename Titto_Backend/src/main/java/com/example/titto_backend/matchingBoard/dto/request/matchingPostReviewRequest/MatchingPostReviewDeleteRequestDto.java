package com.example.titto_backend.matchingBoard.dto.request.matchingPostReviewRequest;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MatchingPostReviewDeleteRequestDto {

    @NotNull
    private Long reviewId;
    @NotNull
    private Long postId;

}
