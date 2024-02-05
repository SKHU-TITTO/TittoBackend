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
public class MatchingPostCreateResponseDto {

    private Long matchingPostId;
    private String author;
    private String category;
    private String status;
    private String title;
    private String content;
    private Integer viewCount;
    private Integer reviewCount;
    private LocalDateTime createDate;

    public static MatchingPostCreateResponseDto of(
            MatchingPost matchingPost) {
        return new MatchingPostCreateResponseDto(matchingPost.getMatchingPostId(),
                matchingPost.getUser().getNickname(),
                String.valueOf(matchingPost.getCategory()),
                String.valueOf(matchingPost.getStatus()),
                matchingPost.getTitle(),
                matchingPost.getContent(),
                matchingPost.getViewCount(),
                matchingPost.getReview_count(),
                matchingPost.getCreateDate());
    }
}

