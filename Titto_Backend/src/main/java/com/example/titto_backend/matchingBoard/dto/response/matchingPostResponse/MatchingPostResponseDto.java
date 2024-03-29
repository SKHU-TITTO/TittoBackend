package com.example.titto_backend.matchingBoard.dto.response.matchingPostResponse;

import com.example.titto_backend.matchingBoard.domain.matchingBoard.MatchingPost;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MatchingPostResponseDto {

    private Long matchingPostId;
    private Long matchingPostAuthorId;
    private String authorNickName;
    private String profile;
    private String category;
    private String status;
    private String title;
    private String content;
    private Integer viewCount;
    private Integer reviewCount;
    private LocalDateTime updateDate;
    private Integer level;

    public static MatchingPostResponseDto of(
            MatchingPost matchingPost) {
        return new MatchingPostResponseDto(
                matchingPost.getMatchingPostId(),
                matchingPost.getUser().getId(),
                matchingPost.getUser().getNickname(),
                matchingPost.getUser().getProfile(),
                String.valueOf(matchingPost.getCategory()),
                String.valueOf(matchingPost.getStatus()),
                matchingPost.getTitle(),
                matchingPost.getContent(),
                matchingPost.getViewCount(),
                matchingPost.getReviewCount(),
                matchingPost.getUpdateDate(),
                matchingPost.getUser().getLevel());
    }

}
