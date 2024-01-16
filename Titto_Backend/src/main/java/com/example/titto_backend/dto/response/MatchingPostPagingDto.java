package com.example.titto_backend.dto.response;

import lombok.Data;

@Data
public class MatchingPostPagingDto {
    private int page;
    private int size;
    private String sort;
}
