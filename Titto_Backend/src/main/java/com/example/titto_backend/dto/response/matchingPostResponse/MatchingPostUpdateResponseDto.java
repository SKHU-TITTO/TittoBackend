package com.example.titto_backend.dto.response.matchingPostResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MatchingPostUpdateResponseDto {

    private Long matchingPostId;
    private String author;
    private String category;
    private String status;
    private String title;
    private String content;
    private Integer viewCount;
    private Integer reviewCount;
    private LocalDateTime updateDate;

    public static MatchingPostUpdateResponseDto of(
            Long matchingPostId,
            String author,
            String category,
            String status,
            String title,
            String content,
            Integer viewCount,
            Integer reviewCount,
            LocalDateTime updateDate) {
        return new MatchingPostUpdateResponseDto(
                matchingPostId,
                author,
                category,
                status,
                title,
                content,
                viewCount,
                reviewCount,
                updateDate);
    }
}
