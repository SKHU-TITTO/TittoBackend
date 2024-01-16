package com.example.titto_backend.dto.request;

import lombok.Data;

@Data
public class MatchingPostPagingRequestDto {
    private int page;
    private int size;
    private String sort;
}
