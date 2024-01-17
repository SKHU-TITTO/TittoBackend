package com.example.titto_backend.dto.response.matchingPostReviewResponse;

import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
public class MatchingReviewUpdateResponseDto {
    private Long reviewId;

    private Long postId;

    private String author;

    private String content;

    private LocalDateTime updateDate;

    public static MatchingReviewUpdateResponseDto of(
            Long reviewId,
            Long postId,
            String author,
            String content,
            LocalDateTime updateDate){
        return new MatchingReviewUpdateResponseDto(reviewId, postId, author, content, updateDate);
    }
}
