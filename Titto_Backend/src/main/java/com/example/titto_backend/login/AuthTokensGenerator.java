package com.example.titto_backend.login;

import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthTokensGenerator {
  private static final String BEARER_TYPE = "Bearer";
  private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30; // 30분
  private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7; // 7일

  private final JwtTokenProvider jwtTokenProvider;

  public AuthTokens generate(Long userId) {
    long now = (new Date()).getTime();
    Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
    Date refreshTokenExpiresIn = new Date(now + REFRESH_TOKEN_EXPIRE_TIME);

    String subject = userId.toString();
    String accessToken = jwtTokenProvider.generate(subject, accessTokenExpiresIn);
    String refreshToken = jwtTokenProvider.generate(subject, refreshTokenExpiresIn);

    return AuthTokens.of(accessToken, refreshToken, BEARER_TYPE, ACCESS_TOKEN_EXPIRE_TIME / 1000L);
  }

  public Long extractUserId(String accessToken) {
    return Long.parseLong(jwtTokenProvider.extractSubject(accessToken));
  }
}