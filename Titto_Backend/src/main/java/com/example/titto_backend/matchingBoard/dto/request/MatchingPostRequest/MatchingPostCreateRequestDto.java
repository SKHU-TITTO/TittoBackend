package com.example.titto_backend.matchingBoard.dto.request.MatchingPostRequest;

import com.example.titto_backend.auth.domain.User;
import com.example.titto_backend.matchingBoard.domain.matchingBoard.Category;
import com.example.titto_backend.matchingBoard.domain.matchingBoard.MatchingPost;
import com.example.titto_backend.matchingBoard.domain.matchingBoard.Status;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MatchingPostCreateRequestDto {

    @NotNull
    private String category;

    @NotNull
    private String title;

    @NotNull
    private String content;

    @NotNull
    private String status;

    public MatchingPost toEntity(User user) {
        return MatchingPost.builder()
                .category(Category.valueOf(category))
                .status(Status.valueOf(status))
                .title(title)
                .content(content)
                .user(user)
                .build();
    }
}
