package com.example.titto_backend.dto.request.MatchingPostRequest;

import com.example.titto_backend.domain.matchingBoard.Category;
import com.example.titto_backend.domain.matchingBoard.MatchingPost;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MatchingPostCreateRequestDto {
    @NotNull
    private Long userId;

    @NotNull
    private Category category;

    @NotNull
    private String title;

    @NotNull
    private String content;

    public MatchingPost toEntity() {
        return MatchingPost.builder()
                .title(title)
                .content(content)
                .build();
    }
}
