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
public class MatchingPostUpdateResponseDto {

    private Long matchingPostId;
    private String author;
    private String profile;
    private String category;
    private String status;
    private String title;
    private String content;
    private Integer viewCount;
    private Integer reviewCount;
    private LocalDateTime updateDate;

    public static MatchingPostUpdateResponseDto of(
            MatchingPost matchingPost) {
        return new MatchingPostUpdateResponseDto(matchingPost.getMatchingPostId(),
                matchingPost.getUser().getNickname(),
                matchingPost.getUser().getProfile(),
                String.valueOf(matchingPost.getCategory()),
                String.valueOf(matchingPost.getStatus()),
                matchingPost.getTitle(),
                matchingPost.getContent(),
                matchingPost.getViewCount(),
                matchingPost.getReviewCount(),
                matchingPost.getUpdateDate());
    }

}
