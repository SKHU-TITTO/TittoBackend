package com.example.titto_backend.matchingBoard.dto.response.matchingPostResponse;

import com.example.titto_backend.matchingBoard.domain.matchingBoard.Category;
import com.example.titto_backend.matchingBoard.domain.matchingBoard.Status;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
public class MatchingPostResponseDto {
    private Long matchingPostId;
    private String authorNickName;
    private Category category;
    private Status status;
    private String title;
    private String content;
    private Integer viewCount;
    private Integer reviewCount;
    private LocalDateTime updateDate;

    public static MatchingPostResponseDto of(
            Long matchingPostId,
            String authorNickName,
            Category category,
            Status status,
            String title,
            String content,
            Integer viewCount,
            Integer reviewCount,
            LocalDateTime updateDate) {
        return new MatchingPostResponseDto(matchingPostId, authorNickName, category, status, title, content, viewCount,reviewCount, updateDate);
    }

}
