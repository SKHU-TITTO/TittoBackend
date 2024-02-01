package com.example.titto_backend.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

public class TokenDTO {

  @Data
  @Builder
  @AllArgsConstructor
  @Schema(description = "카카오 발급 토큰 DTO")
  public static class KakaoToken {

    @Schema(description = "카카오 엑세스 토큰")
    private String kakaoAccessToken;

    @Schema(description = "카카오 리프레시 토큰")
    private String kakaoRefreshToken;

  }

  @Data
  @Builder
  @AllArgsConstructor
  @Schema(description = "네이버 발급 토큰 DTO")
  public static class NaverToken {

    @Schema(description = "네이버 엑세스 토큰")
    private String naverAccessToken;

    @Schema(description = "네이버 리프레시 토큰")
    private String naverRefreshToken;
  }

  @Data
  @Builder
  @AllArgsConstructor
  @Schema(description = "서버 발급 토큰 DTO")
  public static class ServiceToken {

    @Schema(description = "서버 엑세스 토큰")
    private String accessToken;

    @Schema(description = "서버 리프레시 토큰")
    private String refreshToken;

  }

}