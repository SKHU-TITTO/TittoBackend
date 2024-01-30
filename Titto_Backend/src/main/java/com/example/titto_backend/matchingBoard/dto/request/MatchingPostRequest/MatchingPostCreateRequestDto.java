package com.example.titto_backend.matchingBoard.dto.request.MatchingPostRequest;

import com.example.titto_backend.matchingBoard.domain.matchingBoard.Category;
import com.example.titto_backend.matchingBoard.domain.matchingBoard.MatchingPost;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MatchingPostCreateRequestDto {

    @NotNull
    private Category category;

    @NotNull
    private String title;

    @NotNull
    private String content;

    public MatchingPost toEntity() {
        return MatchingPost.builder()
                .category(category)
                .title(title)
                .content(content)
                .build();
    }
}
