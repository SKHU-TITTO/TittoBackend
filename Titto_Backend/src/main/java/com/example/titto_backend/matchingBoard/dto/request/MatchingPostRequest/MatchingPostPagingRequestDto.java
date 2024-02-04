package com.example.titto_backend.matchingBoard.dto.request.MatchingPostRequest;

import lombok.Data;


@Data
public class MatchingPostPagingRequestDto {

    private int page;
    private int size;
    private String sort;

}
