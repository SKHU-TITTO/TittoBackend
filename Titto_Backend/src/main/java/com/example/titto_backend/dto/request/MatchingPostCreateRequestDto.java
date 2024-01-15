package com.example.titto_backend.dto.request;

import com.example.titto_backend.domain.MatchingBoard.Category;
import com.example.titto_backend.domain.MatchingBoard.MatchingPost;
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
