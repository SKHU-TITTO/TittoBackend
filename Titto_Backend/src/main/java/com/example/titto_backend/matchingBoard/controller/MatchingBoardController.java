package com.example.titto_backend.matchingBoard.controller;

import com.example.titto_backend.matchingBoard.dto.response.matchingPostResponse.MatchingPostPagingResponseDto;
import com.example.titto_backend.matchingBoard.service.matchingBoard.MatchingBoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/matching-board")
@RequiredArgsConstructor
@Tag(name = "Matching Board Controller", description = "매칭 게시판 관련 API")
public class MatchingBoardController {

    private final MatchingBoardService matchingBoardService;

    @GetMapping("/all")
    @Operation(
            summary = "매칭 게시판 전체 조회",
            description = "매칭 게시판의 전체 게시글을 조회합니다",
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청 성공"),
                    @ApiResponse(responseCode = "403", description = "인증 문제 발생"),
                    @ApiResponse(responseCode = "500", description = "관리자 문의")
            })
    public MatchingPostPagingResponseDto getAllMatchingPosts(
            @RequestParam("page") int page
    ) {
        return matchingBoardService.findAllPosts(page);
    }

    @GetMapping("/search")
    @Operation(
            summary = "매칭 게시판 검색",
            description = "키워드로 검색하여 결과를 출력합니다",
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청 성공"),
                    @ApiResponse(responseCode = "403", description = "인증 문제 발생"),
                    @ApiResponse(responseCode = "500", description = "관리자 문의")
            })
    public MatchingPostPagingResponseDto searchByKeyWord(@RequestParam("page") int page,
                                                         @RequestParam String keyWord) {
        return matchingBoardService.searchByKeyWord(page, keyWord);
    }

    @GetMapping("/category")
    @Operation(
            summary = "매칭 게시판 카테고리 분류",
            description = "카테고리 별로 결과를 출력합니다",
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청 성공"),
                    @ApiResponse(responseCode = "403", description = "인증 문제 발생"),
                    @ApiResponse(responseCode = "500", description = "관리자 문의")
            })
    public MatchingPostPagingResponseDto findByCategory(@RequestParam("page") int page,
                                                        @RequestParam String category) {
        return matchingBoardService.findByCategory(page, category);
    }
}
