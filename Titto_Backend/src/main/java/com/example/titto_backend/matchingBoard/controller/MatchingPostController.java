package com.example.titto_backend.matchingBoard.controller;

import com.example.titto_backend.matchingBoard.dto.request.MatchingPostRequest.MatchingPostCreateRequestDto;
import com.example.titto_backend.matchingBoard.dto.request.MatchingPostRequest.MatchingPostUpdateRequestDto;
import com.example.titto_backend.matchingBoard.dto.response.matchingPostResponse.MatchingPostCreateResponseDto;
import com.example.titto_backend.matchingBoard.dto.response.matchingPostResponse.MatchingPostDeleteResponseDto;
import com.example.titto_backend.matchingBoard.dto.response.matchingPostResponse.MatchingPostResponseDto;
import com.example.titto_backend.matchingBoard.dto.response.matchingPostResponse.MatchingPostUpdateResponseDto;
import com.example.titto_backend.matchingBoard.service.matchingBoard.MatchingPostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/matching-post")
@RequiredArgsConstructor
@Tag(name = "Matching Post Controller", description = "매칭 게시글 관련 API")
public class MatchingPostController {

    private final MatchingPostService matchingPostService;

    @PostMapping("/create")
    @Operation(
            summary = "매칭 게시글 작성",
            description = "매칭 게시글을 작성합니다",
            responses = {
                    @ApiResponse(responseCode = "201", description = "요청 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "500", description = "관리자 문의")
            })
    public ResponseEntity<MatchingPostCreateResponseDto> createMatchingPost(Principal principal,
                                                                            @RequestBody MatchingPostCreateRequestDto matchingPostCreateRequestDto) {
        MatchingPostCreateResponseDto responseDto = matchingPostService.createMatchingPost(principal,
                matchingPostCreateRequestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @GetMapping("/get/{matchingPostId}")
    @Operation(
            summary = "매칭 게시글 조회",
            description = "특정 매칭 게시글을 조회합니다",
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "500", description = "관리자 문의")
            })
    public ResponseEntity<MatchingPostResponseDto> getMatchingPostByMatchingPostId(@PathVariable Long matchingPostId,
                                                                                   HttpServletRequest request,
                                                                                   HttpServletResponse response) {
        MatchingPostResponseDto responseDto = matchingPostService.findByMatchingPostId(matchingPostId, request,
                response);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PutMapping("/update/{matchingPostId}")
    @Operation(
            summary = "매칭 게시글 수정",
            description = "특정 매칭 게시글을 수정합니다",
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "500", description = "관리자 문의")
            })
    public ResponseEntity<MatchingPostUpdateResponseDto> updateMatchingPost(@PathVariable Long matchingPostId,
                                                                            Principal principal,
                                                                            @RequestBody MatchingPostUpdateRequestDto matchingPostUpdateRequestDto) {
        MatchingPostUpdateResponseDto responseDto = matchingPostService.updateMatchingPost(matchingPostId, principal,
                matchingPostUpdateRequestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{matchingPostId}")
    @Operation(
            summary = "매칭 게시글 삭제",
            description = "특정 매칭 게시글을 삭제합니다",
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "500", description = "관리자 문의")
            })
    public ResponseEntity<MatchingPostDeleteResponseDto> deleteMatchingPostByMatchingPostId(
            @PathVariable Long matchingPostId,
            Principal principal) {
        MatchingPostDeleteResponseDto responseDto = matchingPostService.deleteMatchingPostByMatchingPostId(
                matchingPostId, principal);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @ExceptionHandler(value = AuthorizationServiceException.class)
    public ResponseEntity<String> handleAuthorizationException(AuthorizationServiceException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
    }
}
