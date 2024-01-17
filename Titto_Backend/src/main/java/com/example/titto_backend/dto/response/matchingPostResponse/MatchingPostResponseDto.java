package com.example.titto_backend.dto.response.matchingPostResponse;

import com.example.titto_backend.domain.matchingBoard.Category;
import com.example.titto_backend.domain.matchingBoard.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
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