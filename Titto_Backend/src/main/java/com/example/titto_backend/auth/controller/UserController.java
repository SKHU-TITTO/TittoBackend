package com.example.titto_backend.auth.controller;

import com.example.titto_backend.auth.dto.request.SignUpDTO;
import com.example.titto_backend.auth.dto.request.UserInfoUpdateDTO;
import com.example.titto_backend.auth.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Tag(name = "User Controller", description = "유저 관련 API")
public class UserController {
  private final UserService userService;

  @PutMapping("/signup")
  @Operation(
          summary = "회원가입",
          description = "회원가입을 진행합니다(카카오, 네이버 로그인 후 진행)",
          responses = {
                  @ApiResponse(responseCode = "200", description = "요청 성공"),
                  @ApiResponse(responseCode = "500", description = "관리자 문의")
          })
  public String signUp(@RequestBody SignUpDTO signUpDTO, Principal principal) {
    userService.signUp(signUpDTO, principal.getName());
    return "success";
  }

  @PutMapping("/update")
  @Operation(
          summary = "닉네임과 학번 수정",
          description = "닉네임과 학번을 수정합니다. 닉네임 또는 학번 중 하나를 수정할 수 있습니다.",
          responses = {
                  @ApiResponse(responseCode = "200", description = "요청 성공"),
                  @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                  @ApiResponse(responseCode = "500", description = "관리자 문의")
          })
  public ResponseEntity<String> updateNicknameAndStudentNo(
          @RequestBody UserInfoUpdateDTO requestDTO,
          Principal principal) {
    String userEmail = principal.getName();
    userService.updateNicknameAndStudentNo(userEmail, requestDTO);
    return ResponseEntity.ok("updated successfully");
  }
}
