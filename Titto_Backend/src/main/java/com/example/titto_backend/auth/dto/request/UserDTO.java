package com.example.titto_backend.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

public class UserDTO {

    @Data
    @Builder
    @Schema(description = "로그인 요청 DTO")
    public static class LoginRequest {

        @Schema(description = "카카오 로그인 발급토큰(카카오 로그인 시)")
        private String kakaoAccessToken;

        @Schema(description = "네이버 로그인 발급토큰(네이버 로그인 시)")
        private String naverAccessToken;
    }
}
