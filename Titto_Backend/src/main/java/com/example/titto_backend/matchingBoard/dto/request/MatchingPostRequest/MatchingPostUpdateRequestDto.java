package com.example.titto_backend.matchingBoard.dto.request.MatchingPostRequest;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MatchingPostUpdateRequestDto {

    @NotNull
    private String category;

    @NotNull
    private String title;

    @NotNull
    private String content;

    @NotNull
    private String status;

}
