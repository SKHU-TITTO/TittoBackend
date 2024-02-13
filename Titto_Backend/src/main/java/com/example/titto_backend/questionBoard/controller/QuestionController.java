package com.example.titto_backend.questionBoard.controller;

import com.example.titto_backend.questionBoard.dto.QuestionDTO;
import com.example.titto_backend.questionBoard.service.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/questions")
@Tag(name = "Question Controller", description = "질문 게시판 관련 API")
public class QuestionController {

  private final QuestionService questionService;


  @PostMapping("/create")
  @Operation(
          summary = "질문 작성",
          description = "질문을 작성합니다",
          responses = {
                  @ApiResponse(responseCode = "201", description = "질문 작성 성공"),
                  @ApiResponse(responseCode = "500", description = "관리자 문의")
          })
  public ResponseEntity<QuestionDTO.Response> createQuestion(@RequestBody QuestionDTO.Request request,
                                                             Principal principal) {
    String email = principal.getName(); // 사용자의 이메일을 가져옴

    QuestionDTO.Response savedQuestion = questionService.save(email, request);
    return ResponseEntity.status(HttpStatus.CREATED).body(savedQuestion);
//    try {
//      QuestionDTO.Response savedQuestion = questionService.save(email, request);
//      return ResponseEntity.status(HttpStatus.CREATED).body(savedQuestion);
//    } catch (Exception e) {
//      System.out.println(e.getMessage());
//      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//    }
  }

  @ResponseBody
  @GetMapping("/posts")
  @Operation(
          summary = "질문 목록 조회",
          description = "질문 목록을 조회합니다",
          responses = {
                  @ApiResponse(responseCode = "200", description = "요청 성공"),
                  @ApiResponse(responseCode = "500", description = "관리자 문의")
          })
  public ResponseEntity<Page<QuestionDTO.Response>> getAllQuestions(@PageableDefault(size=20, sort="createdAt", direction = Sort.Direction.DESC) @Parameter(hidden = true) Pageable pageable) {
    Page<QuestionDTO.Response> questions = questionService.findAll(pageable);
    return ResponseEntity.ok(questions);
  }

  @ResponseBody
  @GetMapping("/{postId}")
  @Operation(
          summary = "질문 상세 조회",
          description = "질문 상세 내용을 조회합니다",
          responses = {
                  @ApiResponse(responseCode = "200", description = "요청 성공"),
                  @ApiResponse(responseCode = "404", description = "질문을 찾을 수 없음")
          })
  public ResponseEntity<QuestionDTO.Response> getQuestionById(@PathVariable("postId") Long postId) {
    try {
      QuestionDTO.Response question = questionService.findById(postId);
      return ResponseEntity.ok(question);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  @ResponseBody
  @GetMapping("/category/{category}")
  @Operation(
          summary = "카테고리별 질문 조회",
          description = "카테고리별 질문을 조회합니다",
          responses = {
                  @ApiResponse(responseCode = "200", description = "요청 성공"),
                  @ApiResponse(responseCode = "404", description = "질문을 찾을 수 없음")
          })
  public ResponseEntity<Page<QuestionDTO.Response>> getQuestionsByCategory(@PathVariable("category") String category,
                                                                           Pageable pageable) {
    Page<QuestionDTO.Response> questions = questionService.findByCategory(pageable, category);
    return ResponseEntity.ok(questions);
  }
  // Update method here
  // Delete method here
}
