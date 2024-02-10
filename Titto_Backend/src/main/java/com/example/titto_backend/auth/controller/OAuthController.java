package com.example.titto_backend.auth.controller;

import com.example.titto_backend.auth.dto.response.TokenDTO;
import com.example.titto_backend.auth.dto.response.TokenDTO.ServiceToken;
import com.example.titto_backend.auth.dto.request.UserDTO;
import com.example.titto_backend.auth.service.OAuthKakaoService;
import com.example.titto_backend.auth.service.OAuthNaverService;
import com.example.titto_backend.auth.service.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth")
@Tag(name = "OAuth Controller", description = "OAuth 관련 API")
public class OAuthController {
  private final TokenService tokenService;
  private final OAuthKakaoService oAuthKakaoService;
  private final OAuthNaverService oAuthNaverService;

  @ResponseBody
  @GetMapping("/kakao")
  @Operation(
          summary = "카카오 토큰 발급",
          description = "지정된 URL을 통해 카카오 로그인시 카카오 토큰을 발급합니다",
          responses = {
                  @ApiResponse(responseCode = "200", description = "요청 성공"),
                  @ApiResponse(responseCode = "500", description = "관리자 문의")
          })
  public TokenDTO.KakaoToken kakaoCallback(@RequestParam String code) {
    return oAuthKakaoService.getToken(code);
  }

  @ResponseBody
  @GetMapping("/naver")
  @Operation(
          summary = "네이버 토큰 발급",
          description = "지정된 URL을 통해 네이버 로그인시 네이버 토큰을 발급합니다",
          responses = {
                  @ApiResponse(responseCode = "200", description = "요청 성공"),
                  @ApiResponse(responseCode = "500", description = "관리자 문의")
          })
  public TokenDTO.NaverToken naverCallback(@RequestParam String code, @RequestParam String state) {
    return oAuthNaverService.getToken(code, state);
  }

  @PostMapping("/kakao/login")
  @Operation(
          summary = "카카오 토큰으로 로그인 (kakaoAcessToken만 입력하세요.)",
          description = "존재하지 않은 유저일 경우 회원가입 진행 후 로그인합니다",
          responses = {
                  @ApiResponse(responseCode = "200", description = "요청 성공"),
                  @ApiResponse(responseCode = "400", description = "카카오 서버에 유저의 이메일이 없음"),
                  @ApiResponse(responseCode = "403", description = "인증 오류 (토큰)")
          })
  public ResponseEntity<ServiceToken> kakaoLogin(@RequestBody UserDTO.LoginRequest dto) {
    TokenDTO.ServiceToken serviceToken = oAuthKakaoService.joinAndLogin(dto);
    return ResponseEntity.ok(serviceToken);
  }

  @PostMapping("/naver/login")
  @Operation(
          summary = "네이버 토큰으로 로그인 (naverAcessToken만 입력하세요.)",
          description = "존재하지 않은 유저일 경우 회원가입 진행 후 로그인합니다",
          responses = {
                  @ApiResponse(responseCode = "200", description = "요청 성공"),
                  @ApiResponse(responseCode = "400", description = "네이버 서버에 유저의 이메일이 없음"),
                  @ApiResponse(responseCode = "403", description = "인증 오류 (토큰)")
          })
  public ResponseEntity<ServiceToken> naverLogin(@RequestBody UserDTO.LoginRequest dto) {
    TokenDTO.ServiceToken serviceToken = oAuthNaverService.joinAndLogin(dto);
    return ResponseEntity.ok(serviceToken);
  }

  @PostMapping("/refresh")
  @Operation(
          summary = "리프레시",
          description = "리프레시 토큰을 통해 엑세스 토큰 유효 기간 초기화",
          responses = {
                  @ApiResponse(responseCode = "200", description = "요청 성공"),
                  @ApiResponse(responseCode = "400", description = "리프레시 토큰 만료"),
                  @ApiResponse(responseCode = "403", description = "인증 오류 (토큰)")
          })
  public ResponseEntity<TokenDTO.ServiceToken> refresh(HttpServletRequest request,
                                                       @RequestBody TokenDTO.ServiceToken dto) {
    TokenDTO.ServiceToken serviceToken = tokenService.refresh(request, dto);
    return ResponseEntity.ok(serviceToken);
  }

  @PostMapping("/logout")
  @Operation(
          summary = "로그아웃",
          description = "액세스 토큰 블랙리스트에 저장 및 리프레시 토큰 제거",
          responses = {
                  @ApiResponse(responseCode = "200", description = "요청 성공"),
                  @ApiResponse(responseCode = "403", description = "인증 오류 (토큰)"),
                  @ApiResponse(responseCode = "500", description = "관리자 문의")
          })
  public ResponseEntity<String> logout(HttpServletRequest request, @RequestBody TokenDTO.ServiceToken dto,
                                       Principal principal) {
    tokenService.logout(request, dto, principal);
    return ResponseEntity.ok("로그아웃 완료");
  }

}
