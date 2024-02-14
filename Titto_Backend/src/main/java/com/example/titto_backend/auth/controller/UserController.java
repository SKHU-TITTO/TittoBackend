package com.example.titto_backend.auth.controller;

import com.example.titto_backend.auth.dto.request.SignUpDTO;
import com.example.titto_backend.auth.dto.response.UserInfoDTO;
import com.example.titto_backend.auth.dto.request.UserInfoUpdateDTO;
import com.example.titto_backend.auth.dto.request.UserProfileUpdateDTO;
import com.example.titto_backend.auth.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "User Controller", description = "유저 관련 API")
public class UserController {

  private final UserService userService;

  @PutMapping("/signup")
  @PreAuthorize("isAuthenticated()")
  @Operation(
          summary = "회원가입",
          description = "회원가입을 진행합니다(카카오, 네이버 로그인 후 진행)",
          responses = {
                  @ApiResponse(responseCode = "200", description = "요청 성공"),
                  @ApiResponse(responseCode = "500", description = "관리자 문의")
          })
  public String signUp(@RequestBody SignUpDTO signUpDTO, @AuthenticationPrincipal UserDetails userDetails) {
    userService.signUp(signUpDTO, userDetails.getUsername());
    return "success";
  }

  // 사용자 정보 불러오기
  @GetMapping("/info")
  @PreAuthorize("isAuthenticated()")
  @Operation(
          summary = "유저 정보 불러오기",
          description = "유저 정보를 불러옵니다.",
          responses = {
                  @ApiResponse(responseCode = "200", description = "요청 성공"),
                  @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                  @ApiResponse(responseCode = "500", description = "관리자 문의")
          })
  public ResponseEntity<UserInfoDTO> getUserInfo(@AuthenticationPrincipal UserDetails userDetails) {
    String email = userDetails.getUsername();
    UserInfoDTO userInfo = userService.getUser(email);
    return ResponseEntity.ok(userInfo);
  }

  @PutMapping("/update")
  @PreAuthorize("isAuthenticated()")
  @Operation(
          summary = "닉네임 수정",
          description = "닉네임 수정이 가능합니다.",
          responses = {
                  @ApiResponse(responseCode = "200", description = "요청 성공"),
                  @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                  @ApiResponse(responseCode = "500", description = "관리자 문의")
          })
  public ResponseEntity<String> updateNicknameAndStudentNo(
          @RequestBody UserInfoUpdateDTO requestDTO,
          @AuthenticationPrincipal UserDetails userDetails) {
    String userEmail = userDetails.getUsername();
    userService.updateNickname(userEmail, requestDTO);
    return ResponseEntity.ok(" updated successfully");
  }

  @PutMapping("/profile")
  @PreAuthorize("isAuthenticated()")
  @Operation(
          summary = "유저 프로필 수정",
          description = "유저 프로필을 수정합니다(한줄소개, 자기소개)",
          responses = {
                  @ApiResponse(responseCode = "200", description = "요청 성공"),
                  @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                  @ApiResponse(responseCode = "500", description = "관리자 문의")
          })
  public ResponseEntity<String> updateUserProfile(@AuthenticationPrincipal UserDetails userDetails,
                                                  @RequestBody UserProfileUpdateDTO userProfileUpdateDTO) {
    String email = userDetails.getUsername();
    userService.updateUserProfile(email, userProfileUpdateDTO);
    return ResponseEntity.ok("updated profile");
  }

  @GetMapping("/check/nickname")
  @Operation(
          summary = "닉네임 중복 확인",
          description = "닉네임 중복 여부를 확인합니다.",
          responses = {
                  @ApiResponse(responseCode = "200", description = "요청 성공"),
                  @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                  @ApiResponse(responseCode = "500", description = "관리자 문의")
          })
  public ResponseEntity<String> checkNickname(@RequestParam String nickname) {
    if (userService.isDuplicatedNickname(nickname)) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body("Nickname is already taken");
    } else {
      return ResponseEntity.ok("Nickname is available");
    }
  }

  @GetMapping("/check/studentNo")
  @Operation(
          summary = "학번 중복 확인",
          description = "학번 중복 여부를 확인합니다.",
          responses = {
                  @ApiResponse(responseCode = "200", description = "요청 성공"),
                  @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                  @ApiResponse(responseCode = "500", description = "관리자 문의")
          })
  public ResponseEntity<String> checkStudentNo(@RequestParam String studentNo) {
    if (userService.isDuplicatedStudentNo(studentNo)) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body("Student number is already taken");
    } else {
      return ResponseEntity.ok("Student number is available");
    }
  }
}