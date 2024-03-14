package com.example.titto_backend.matchingBoard.dto.request.matchingPostReviewRequest;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "게시글 ID")
    private Long postId;

    @NotNull
    @Schema(description = "내용")
    private String content;

}
