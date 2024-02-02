package com.example.titto_backend.matchingBoard.controller;

import com.example.titto_backend.matchingBoard.dto.request.MatchingPostRequest.MatchingPostPagingRequestDto;
import com.example.titto_backend.matchingBoard.dto.response.matchingPostResponse.MatchingPostPagingResponseDto;
import com.example.titto_backend.matchingBoard.service.matchingBoard.MatchingBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/matching-board")
@RequiredArgsConstructor
public class MatchingBoardController {
    private final MatchingBoardService matchingBoardService;

    @GetMapping("/posts")
    public MatchingPostPagingResponseDto getAllMatchingPosts(MatchingPostPagingRequestDto matchingPostPagingRequestDto) {
        return matchingBoardService.findAllPosts(matchingPostPagingRequestDto);
    }
}