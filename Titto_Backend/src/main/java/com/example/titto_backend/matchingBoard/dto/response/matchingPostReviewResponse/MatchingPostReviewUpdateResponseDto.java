package com.example.titto_backend.matchingBoard.dto.response.matchingPostReviewResponse;

import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
public class MatchingPostReviewUpdateResponseDto {
    private Long reviewId;

    private Long postId;

    private String author;

    private String content;

    private LocalDateTime updateDate;

    public static MatchingPostReviewUpdateResponseDto of(
            Long reviewId,
            Long postId,
            String author,
            String content,
            LocalDateTime updateDate){
        return new MatchingPostReviewUpdateResponseDto(reviewId, postId, author, content, updateDate);
    }
}
