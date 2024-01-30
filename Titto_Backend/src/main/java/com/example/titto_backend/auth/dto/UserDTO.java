package com.example.titto_backend.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

public class UserDTO {

  @Data
  @Getter
  @Builder
  @Schema(description = "로그인 요청 DTO")
  public static class LoginRequest {

    @Schema(description = "카카오 로그인 발급토큰")
    private String kakaoAccessToken;

    @Schema(description = "네이버 로그인 발급토큰")
    private String naverAccessToken;
  }
}
