package com.example.titto_backend.feedback.controller;

import com.example.titto_backend.feedback.dto.FeedbackDTO;
import com.example.titto_backend.feedback.service.FeedbackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/feedbacks")
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping
    @Operation(
            summary = "문의 작성",
            description = "문의를 작성합니다",
            responses = {
                    @ApiResponse(responseCode = "201", description = "문의 작성 성공"),
                    @ApiResponse(responseCode = "500", description = "관리자 문의")
            })
    public ResponseEntity<String> writeFeedback(@RequestBody FeedbackDTO.Request request,
                                                Principal principal) {
        String email = principal.getName();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(feedbackService.writeFeedback(email, request));
    }

    @GetMapping
    @Operation(
            summary = "전체 문의 조회",
            description = "전체 문의를 조회합니다",
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청 성공"),
                    @ApiResponse(responseCode = "500", description = "관리자 문의")
            })
    public ResponseEntity<List<FeedbackDTO.Response>> getAllFeedbacks() {
        List<FeedbackDTO.Response> responses = feedbackService.findAllFeedbacks();
        return ResponseEntity.ok(responses);
    }

}
