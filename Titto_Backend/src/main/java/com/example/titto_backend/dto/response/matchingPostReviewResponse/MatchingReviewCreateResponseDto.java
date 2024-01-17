package com.example.titto_backend.dto.response.matchingPostReviewResponse;

import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
public class MatchingReviewCreateResponseDto {
    private Long reviewId;

    private Long postId;

    private String author;

    private String content;

    private LocalDateTime createDate;

    public static MatchingReviewCreateResponseDto of(
            Long reviewId,
            Long postId,
            String author,
            String content,
            LocalDateTime createDate
    ) {
        return new MatchingReviewCreateResponseDto(reviewId, postId, author, content, createDate);
    }
}
