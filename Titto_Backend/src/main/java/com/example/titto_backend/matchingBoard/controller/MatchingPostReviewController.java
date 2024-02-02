package com.example.titto_backend.matchingBoard.controller;


import com.example.titto_backend.matchingBoard.dto.request.matchingPostReviewRequest.MatchingPostReviewCreateRequestDto;
import com.example.titto_backend.matchingBoard.dto.request.matchingPostReviewRequest.MatchingPostReviewUpdateRequestDto;
import com.example.titto_backend.matchingBoard.dto.response.matchingPostReviewResponse.MatchingPostReviewCreateResponseDto;
import com.example.titto_backend.matchingBoard.dto.response.matchingPostReviewResponse.MatchingPostReviewDeleteResponseDto;
import com.example.titto_backend.matchingBoard.dto.response.matchingPostReviewResponse.MatchingPostReviewResponseDto;
import com.example.titto_backend.matchingBoard.dto.response.matchingPostReviewResponse.MatchingPostReviewUpdateResponseDto;
import com.example.titto_backend.matchingBoard.service.matchingBoardReview.MatchingBoardReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/matching-board-reviews")
@RequiredArgsConstructor
public class MatchingPostReviewController {
    private final MatchingBoardReviewService matchingBoardReviewService;

    @PostMapping
    public ResponseEntity<MatchingPostReviewCreateResponseDto> createReview(Principal principal,
                                                                            @RequestBody MatchingPostReviewCreateRequestDto matchingPostReviewCreateRequestDto) {
        MatchingPostReviewCreateResponseDto responseDto = matchingBoardReviewService.createReview(principal, matchingPostReviewCreateRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<List<MatchingPostReviewResponseDto>> getAllMatchingBoardReviewsByPostId(@PathVariable Long postId) {
        List<MatchingPostReviewResponseDto> responseDtoList = matchingBoardReviewService.getAllMatchingBoardReviewsByPostId(postId);
        return ResponseEntity.ok(responseDtoList);
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<MatchingPostReviewUpdateResponseDto> updateReview(Principal principal,
                                                                            @RequestBody MatchingPostReviewUpdateRequestDto matchingPostReviewUpdateRequestDto) {
        MatchingPostReviewUpdateResponseDto responseDto = matchingBoardReviewService.updateReview(principal, matchingPostReviewUpdateRequestDto);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<MatchingPostReviewDeleteResponseDto> deleteReviewByReviewId(@PathVariable Long reviewId) {
        MatchingPostReviewDeleteResponseDto responseDto = matchingBoardReviewService.deleteReviewByReviewId(reviewId);
        return ResponseEntity.ok(responseDto);
    }
}