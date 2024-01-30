package com.example.titto_backend.matchingBoard.dto.response.matchingPostResponse;

import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
public class MatchingPostCreateResponseDto {

    private Long matchingPostId;
    private String author;
    private String category;
    private String status;
    private String title;
    private String content;
    private Integer viewCount;
    private Integer reviewCount;
    private LocalDateTime createDate;

    public static MatchingPostCreateResponseDto of(
            Long matchingPostId,
            String author,
            String category,
            String status,
            String title,
            String content,
            Integer viewCount,
            Integer reviewCount,
            LocalDateTime createDate) {
        return new MatchingPostCreateResponseDto(matchingPostId, author, category, status, title, content, viewCount, reviewCount, createDate);
    }
}

