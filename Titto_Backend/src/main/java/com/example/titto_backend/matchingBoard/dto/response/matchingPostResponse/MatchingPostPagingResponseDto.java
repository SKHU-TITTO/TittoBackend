package com.example.titto_backend.matchingBoard.dto.response.matchingPostResponse;

import com.example.titto_backend.matchingBoard.domain.matchingBoard.MatchingPost;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MatchingPostPagingResponseDto {

    private List<MatchingPost> content;
    private int totalPages;
    private long totalElements;
    private int pageNumber;
    private boolean existNextPage;

    public static MatchingPostPagingResponseDto from(Page<MatchingPost> postPages) {
        return MatchingPostPagingResponseDto.builder()
                .content(postPages.getContent())
                .totalPages(postPages.getTotalPages())
                .totalElements(postPages.getTotalElements())
                .pageNumber(postPages.getNumber())
                .existNextPage(postPages.hasNext())
                .build();
    }

}
