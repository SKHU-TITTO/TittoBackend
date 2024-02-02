package com.example.titto_backend.matchingBoard.dto.response.matchingPostResponse;

import com.example.titto_backend.matchingBoard.domain.matchingBoard.MatchingPost;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

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
            MatchingPost matchingPost,
            Integer reviewCount) {
        return new MatchingPostUpdateResponseDto(matchingPost.getMatchingPostId(),
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
