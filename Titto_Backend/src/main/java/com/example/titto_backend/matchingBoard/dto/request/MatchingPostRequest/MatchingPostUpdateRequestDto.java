package com.example.titto_backend.matchingBoard.dto.request.MatchingPostRequest;

import com.example.titto_backend.matchingBoard.domain.matchingBoard.Category;
import com.example.titto_backend.matchingBoard.domain.matchingBoard.MatchingPost;
import com.example.titto_backend.matchingBoard.domain.matchingBoard.Status;

import jakarta.validation.constraints.NotNull;

public class MatchingPostUpdateRequestDto {
    @NotNull
    private String category;

    @NotNull
    private String title;

    @NotNull
    private String content;

    @NotNull
    private String status;

    public MatchingPost toEntity() {
        return MatchingPost.builder()
                .category(Category.valueOf(category))
                .status(Status.valueOf(status))
                .title(title)
                .content(content)
                .build();
    }
}
