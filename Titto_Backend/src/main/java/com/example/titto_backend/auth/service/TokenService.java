package com.example.titto_backend.auth.service;

import com.example.titto_backend.auth.dto.response.TokenDTO;
import com.example.titto_backend.auth.jwt.TokenProvider;
import com.example.titto_backend.common.exception.CustomException;
import com.example.titto_backend.common.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.security.Principal;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TokenService {
  private final TokenProvider tokenProvider;
  private final RedisTemplate<String, Object> redisTemplate;

  public TokenDTO.ServiceToken refresh(HttpServletRequest request, TokenDTO.ServiceToken dto) {
    String refreshToken = dto.getRefreshToken();

    String isValidate = (String) redisTemplate.opsForValue().get(refreshToken);
    if (ObjectUtils.isEmpty(isValidate)) {
      throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
    }
    return tokenProvider.createAccessTokenByRefreshToken(request, refreshToken);
  }

  public void logout(HttpServletRequest request, TokenDTO.ServiceToken dto, Principal principal) {
    String accessToken = tokenProvider.resolveToken(request);

    Long expireTime = tokenProvider.getExpiration(accessToken);

    redisTemplate.opsForValue().set(accessToken, "logout", expireTime, TimeUnit.MILLISECONDS);

    redisTemplate.delete(dto.getRefreshToken());
  }
}

