package com.example.titto_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MatchingPostCreateResponseDto {

    private Long matchingPostId;
    private String author;
    private String category;
    private String status;
    private String title;
    private String content;
    private Integer viewCount;
    private LocalDateTime createDate;

    public static MatchingPostCreateResponseDto of(
            Long matchingPostId,
            String author,
            String category,
            String status,
            String title,
            String content,
            Integer viewCount,
            LocalDateTime createDate) {
        return new MatchingPostCreateResponseDto(matchingPostId, author, category, status, title, content, viewCount, createDate);
    }
}

