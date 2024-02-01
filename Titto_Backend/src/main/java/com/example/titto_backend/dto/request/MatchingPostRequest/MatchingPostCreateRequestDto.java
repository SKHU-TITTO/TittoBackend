package com.example.titto_backend.dto.request.MatchingPostRequest;

import com.example.titto_backend.domain.matchingBoard.Category;
import com.example.titto_backend.domain.matchingBoard.MatchingPost;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MatchingPostCreateRequestDto {

    @NotNull
    private String category;

    @NotNull
    private String title;

    @NotNull
    private String content;

    public MatchingPost toEntity() {
        return MatchingPost.builder()
                .category(Category.valueOf(category))
                .title(title)
                .content(content)
                .build();
    }
}
