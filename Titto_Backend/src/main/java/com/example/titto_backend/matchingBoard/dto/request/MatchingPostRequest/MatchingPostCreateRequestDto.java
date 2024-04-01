package com.example.titto_backend.matchingBoard.dto.request.MatchingPostRequest;

import com.example.titto_backend.auth.domain.User;
import com.example.titto_backend.matchingBoard.domain.matchingBoard.Category;
import com.example.titto_backend.matchingBoard.domain.matchingBoard.MatchingPost;
import com.example.titto_backend.matchingBoard.domain.matchingBoard.Status;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "카테고리", example = "STUDY, MENTOR, MENTEE, UHWOOLLEAM")
    private String category;

    @NotNull
    @Schema(description = "제목")
    private String title;

    @NotNull
    @Schema(description = "내용")
    private String content;

    @NotNull
    @Schema(description = "상태", defaultValue = "RECRUITING")
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
