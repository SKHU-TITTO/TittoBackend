package com.example.titto_backend.matchingBoard.dto.response.matchingPostReviewResponse;

import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
public class MatchingPostReviewCreateResponseDto {
    private Long reviewId;

    private Long postId;

    private String author;

    private String content;

    private LocalDateTime createDate;

    public static MatchingPostReviewCreateResponseDto of(
            Long reviewId,
            Long postId,
            String author,
            String content,
            LocalDateTime createDate
    ) {
        return new MatchingPostReviewCreateResponseDto(reviewId, postId, author, content, createDate);
    }
}
