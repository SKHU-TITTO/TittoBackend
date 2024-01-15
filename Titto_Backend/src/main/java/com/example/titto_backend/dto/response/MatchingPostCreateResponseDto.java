package com.example.titto_backend.dto.response;

import com.example.titto_backend.domain.MatchingBoard.Category;
import com.example.titto_backend.domain.MatchingBoard.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MatchingPostCreateResponseDto {

    private Long matchingPostId;
    private UserResponseDto user;
    private Category category;
    private Status status;
    private String title;
    private String content;
    private Integer viewCount;

    public static MatchingPostCreateResponseDto of(Long matchingPostId, UserResponseDto user, Category category, Status status, String title, String content, Integer viewCount) {
        return new MatchingPostCreateResponseDto(matchingPostId, user, category, status, title, content, viewCount);
    }
}
