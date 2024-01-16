package com.example.titto_backend.dto.response;

import com.example.titto_backend.domain.MatchingBoard.Category;
import com.example.titto_backend.domain.MatchingBoard.Status;
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
    private LocalDateTime createDate;


    public static MatchingPostResponseDto of(
            Long matchingPostId,
            String authorNickName,
            Category category,
            Status status,
            String title,
            String content,
            Integer viewCount,
            LocalDateTime createDate) {
        return new MatchingPostResponseDto(matchingPostId, authorNickName, category, status, title, content, viewCount, createDate);
    }

}
