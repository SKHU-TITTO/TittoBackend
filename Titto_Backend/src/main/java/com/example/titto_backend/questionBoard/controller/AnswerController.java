package com.example.titto_backend.questionBoard.controller;

import com.example.titto_backend.auth.domain.User;
import com.example.titto_backend.auth.repository.UserRepository;
import com.example.titto_backend.common.exception.CustomException;
import com.example.titto_backend.common.exception.ErrorCode;
import com.example.titto_backend.questionBoard.dto.AnswerDTO;
import com.example.titto_backend.questionBoard.service.AnswerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/answers")
@Tag(name = "Answer Controller", description = "게시글 답변 관련 API")
public class AnswerController {

    private final AnswerService answerService;
    private final UserRepository userRepository;

    @PostMapping("/create")
    @Operation(
            summary = "답변 작성",
            description = "답변을 작성합니다",
            responses = {
                    @ApiResponse(responseCode = "201", description = "답변 작성 성공"),
                    @ApiResponse(responseCode = "500", description = "관리자 문의")
            })
    public ResponseEntity<AnswerDTO.Response> createAnswer(@RequestBody AnswerDTO.Request request,
                                                           Principal principal) {
        String email = principal.getName();
        AnswerDTO.Response savedAnswer = answerService.save(request, request.getQuestionId(), email);

        return ResponseEntity.status(201).body(savedAnswer);
    }

    @PutMapping("/accept/{answerId}")
    @Operation(
            summary = "답변 채택",
            description = "답변을 채택합니다",
            responses = {
                    @ApiResponse(responseCode = "200", description = "답변 채택 성공"),
                    @ApiResponse(responseCode = "404", description = "답변을 찾을 수 없음")
            })
    public ResponseEntity<String> acceptAnswer(@PathVariable("answerId") Long answerId,
                                               @RequestBody Long questionId) {
        String currentEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByEmail(currentEmail)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        answerService.acceptAnswer(questionId, answerId, currentUser.getId());
        return ResponseEntity.ok("답변 채택 성공");
    }

    @PutMapping("/{answerId}")
    @Operation(
            summary = "답변 수정",
            description = "답변을 수정합니다",
            responses = {
                    @ApiResponse(responseCode = "200", description = "답변 수정 성공"),
                    @ApiResponse(responseCode = "404", description = "답변을 찾을 수 없음")
            })
    public ResponseEntity<AnswerDTO.Response> updateAnswer(@PathVariable("answerId") Long answerId,
                                                           @RequestBody AnswerDTO.Request request) {
        String currentEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByEmail(currentEmail)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        return ResponseEntity.ok(answerService.update(answerId, request, currentUser.getId()));
    }

    @DeleteMapping("/{answerId}")
    @Operation(
            summary = "답변 삭제",
            description = "답변을 삭제합니다",
            responses = {
                    @ApiResponse(responseCode = "204", description = "답변 삭제 성공"),
                    @ApiResponse(responseCode = "500", description = "관리자 문의")
            })
    public ResponseEntity<Void> deleteAnswer(@PathVariable("answerId") Long answerId) {
        String currentEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByEmail(currentEmail)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        answerService.delete(answerId, currentUser.getId());
        return ResponseEntity.noContent().build();
    }

}
