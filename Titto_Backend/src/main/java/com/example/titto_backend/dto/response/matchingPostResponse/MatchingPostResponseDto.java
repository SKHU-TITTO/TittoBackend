package com.example.titto_backend.dto.response.matchingPostResponse;

import com.example.titto_backend.domain.matchingBoard.MatchingPost;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
public class MatchingPostResponseDto {
    private Long matchingPostId;
    private String authorNickName;
    private String category;
    private String status;
    private String title;
    private String content;
    private Integer viewCount;
    private Integer reviewCount;
    private LocalDateTime updateDate;

    public static  MatchingPostResponseDto of(
            MatchingPost matchingPost,
            Integer reviewCount) {
        return new MatchingPostResponseDto(
                matchingPost.getMatchingPostId(),
                matchingPost.getUser().getNickname(),
                String.valueOf(matchingPost.getCategory()),
                String.valueOf(matchingPost.getStatus()),
                matchingPost.getTitle(),
                matchingPost.getContent(),
                matchingPost.getViewCount(),
                reviewCount,
                matchingPost.getUpdateDate());
    }

}
