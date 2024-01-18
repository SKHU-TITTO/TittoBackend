package com.example.titto_backend.dto.request.matchingPostReviewRequest;

import com.example.titto_backend.domain.matchingBoard.MatchingPost;
import com.example.titto_backend.domain.review.MatchingPostReview;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class MatchingPostReviewCreateRequestDto {

    @NotNull
    private MatchingPost postId;
    @NotNull
    private String content;

    public MatchingPostReview toEntity() {
        return MatchingPostReview.builder()
               .matchingPost(postId)
               .content(content)
               .build();
    }
}
