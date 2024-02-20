package com.example.titto_backend.matchingBoard.dto.response.matchingPostResponse;

import com.example.titto_backend.matchingBoard.domain.matchingBoard.MatchingPost;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MatchingPostResponseDto {

    private Long matchingPostId;
    private String authorNickName;
    private String profile;
    private String category;
    private String status;
    private String title;
    private String content;
    private Integer viewCount;
    private Integer reviewCount;
    private LocalDateTime updateDate;

    public static MatchingPostResponseDto of(
            MatchingPost matchingPost,
            Integer reviewCount) {
        return new MatchingPostResponseDto(
                matchingPost.getMatchingPostId(),
                matchingPost.getUser().getNickname(),
                matchingPost.getUser().getProfile(),
                String.valueOf(matchingPost.getCategory()),
                String.valueOf(matchingPost.getStatus()),
                matchingPost.getTitle(),
                matchingPost.getContent(),
                matchingPost.getViewCount(),
                reviewCount,
                matchingPost.getUpdateDate());
    }

}
