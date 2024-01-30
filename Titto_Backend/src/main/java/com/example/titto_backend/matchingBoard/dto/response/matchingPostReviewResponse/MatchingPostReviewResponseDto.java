package com.example.titto_backend.matchingBoard.dto.response.matchingPostReviewResponse;

import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
public class MatchingPostReviewResponseDto {
    private Long reviewId;

    private Long postId;

    private String author;

    private String content;

    private LocalDateTime updateDate;

    public static MatchingPostReviewResponseDto of(
            Long reviewId,
            Long postId,
            String author,
            String content,
            LocalDateTime updateDate){
        return new MatchingPostReviewResponseDto(reviewId, postId, author, content, updateDate);
    }
}
