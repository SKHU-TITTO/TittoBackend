package com.example.titto_backend.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MatchingPostCreateRequestDto {
    @NotNull
    private Long userId;
    @NotNull
    private String title;
    @NotNull
    private String content;
}
