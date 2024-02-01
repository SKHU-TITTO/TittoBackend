package com.example.titto_backend.matchingBoard.dto.response.matchingPostResponse;

import com.example.titto_backend.matchingBoard.domain.matchingBoard.MatchingPost;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@NoArgsConstructor
public class MatchingPostPagingResponseDto {
    private List<MatchingPost> content;
    private int totalPages;
    private long totalElements;
    private int pageNumber;
    private boolean existNextPage;

    public MatchingPostPagingResponseDto(Page<MatchingPost> postPages) {
        this.content = postPages.getContent();
        this.totalPages = postPages.getTotalPages();
        this.totalElements = postPages.getTotalElements();
        this.pageNumber = postPages.getNumber();
        this.existNextPage = postPages.hasNext();
    }
}
