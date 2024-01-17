package com.example.titto_backend.dto.response.matchingPostReviewResponse;

import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
public class MatchingReviewResponseDto {
    private Long reviewId;

    private Long postId;

    private String author;

    private String content;

    private LocalDateTime updateDate;

    public static MatchingReviewResponseDto of(
            Long reviewId,
            Long postId,
            String author,
            String content,
            LocalDateTime updateDate){
        return new MatchingReviewResponseDto(reviewId, postId, author, content, updateDate);
    }
}
