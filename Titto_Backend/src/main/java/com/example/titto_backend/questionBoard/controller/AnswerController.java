package com.example.titto_backend.questionBoard.controller;

import com.example.titto_backend.auth.domain.User;
import com.example.titto_backend.auth.repository.UserRepository;
import com.example.titto_backend.common.exception.CustomException;
import com.example.titto_backend.common.exception.ErrorCode;
import com.example.titto_backend.questionBoard.dto.AnswerDTO;
import com.example.titto_backend.questionBoard.dto.AnswerDTO.Response;
import com.example.titto_backend.questionBoard.service.AnswerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

  // Create
  @PostMapping("/create")
  @Operation(
          summary = "답변 작성",
          description = "답변을 작성합니다",
          responses = {
                  @ApiResponse(responseCode = "201", description = "답변 작성 성공"),
                  @ApiResponse(responseCode = "500", description = "관리자 문의")
          })
  public ResponseEntity<AnswerDTO.Response> createAnswer(@RequestBody AnswerDTO.Request request, Principal principal) {
    String email = principal.getName();
    AnswerDTO.Response savedAnswer = answerService.save(request, request.getPostId(), email);

    return ResponseEntity.status(201).body(savedAnswer);
  }

  // Read
  @GetMapping("/answers")
  @Operation(
          summary = "답변 목록 조회",
          description = "답변 목록을 조회합니다",
          responses = {
                  @ApiResponse(responseCode = "200", description = "요청 성공"),
                  @ApiResponse(responseCode = "500", description = "관리자 문의")
          })
  public ResponseEntity<Page<Response>> getAllQuestions(@Parameter(hidden = true) Pageable pageable) {
    Page<AnswerDTO.Response> answers = answerService.findAll(pageable);
    return ResponseEntity.ok(answers);
  }

  @GetMapping("/{answerId}")
  @Operation(
          summary = "답변 상세 조회",
          description = "답변 상세를 조회합니다",
          responses = {
                  @ApiResponse(responseCode = "200", description = "요청 성공"),
                  @ApiResponse(responseCode = "404", description = "답변 없음")
          })
  public ResponseEntity<AnswerDTO.Response> getAnswerById(@PathVariable("answerId") Long answerId) {
    try {
      AnswerDTO.Response answer = answerService.findById(answerId);
      return ResponseEntity.ok(answer);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  // Delete
  @DeleteMapping("/{answerId}")
  @Operation(
          summary = "답변 삭제",
          description = "답변을 삭제합니다",
          responses = {
                  @ApiResponse(responseCode = "204", description = "답변 삭제 성공"),
                  @ApiResponse(responseCode = "500", description = "관리자 문의")
          })
  public ResponseEntity<Void> deleteAnswer(@PathVariable("answerId") Long answerId) {
    String currentEmail = SecurityContextHolder.getContext().getAuthentication().getName(); // 현재 사용자의 이메일 주소 가져오기
    User currentUser = userRepository.findByEmail(currentEmail)
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND)); // 현재 사용자의 ID를 이메일 주소로 검색하여 가져오기
    answerService.delete(answerId, currentUser.getId()); // 현재 사용자의 ID를 전달하여 삭제 메소드 호출
    return ResponseEntity.noContent().build();
  }

}
