package com.example.titto_backend.auth.service;

import com.example.titto_backend.auth.domain.User;
import com.example.titto_backend.auth.dto.TokenDTO;
import com.example.titto_backend.auth.dto.UserDTO;
import com.example.titto_backend.auth.jwt.TokenProvider;
import com.example.titto_backend.auth.repository.UserRepository;
import com.example.titto_backend.common.exception.CustomException;
import com.example.titto_backend.common.exception.ErrorCode;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import jakarta.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class OAuthKakaoService {
  private final UserRepository userRepository;
  private final TokenProvider tokenProvider;
  private final RedisTemplate<String, Object> redisTemplate;

  @Value("${oauth.kakao.client-id}")
  private String KAKAO_CLIENT_ID;

  @Value("${oauth.kakao.client-secret}")
  private String KAKAO_CLIENT_SECRET;

  @Value("${oauth.kakao.redirect-uri}")
  private String KAKAO_REDIRECT_URI;

  public TokenDTO.KakaoToken getToken(String code) {
    RestTemplate restTemplate = new RestTemplate();

    String reqURL = "https://kauth.kakao.com/oauth/token";

    MultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
    requestParams.add("grant_type", "authorization_code");
    requestParams.add("client_id", KAKAO_CLIENT_ID);
    requestParams.add("client_secret", KAKAO_CLIENT_SECRET);
    requestParams.add("redirect_uri", KAKAO_REDIRECT_URI);
    requestParams.add("code", code);

    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

    HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestParams, headers);

    ResponseEntity<String> responseEntity = restTemplate.exchange(reqURL, HttpMethod.POST, requestEntity, String.class);

    if (!responseEntity.getStatusCode().is2xxSuccessful()) {
      throw new CustomException(ErrorCode.INVALID_KAKAO_VALUE);
    }

    JsonElement jsonElement = JsonParser.parseString(Objects.requireNonNull(responseEntity.getBody()))
            .getAsJsonObject();

    String accessToken = jsonElement.getAsJsonObject().get("access_token").getAsString();
    String refreshToken = jsonElement.getAsJsonObject().get("refresh_token").getAsString();

    return new TokenDTO.KakaoToken(accessToken, refreshToken);
  }

  @Transactional
  public TokenDTO.ServiceToken joinAndLogin(UserDTO.LoginRequest dto) {
    RestTemplate restTemplate = new RestTemplate();
    String reqURL = "https://kapi.kakao.com/v2/user/me";

    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
    headers.set("Authorization", "Bearer " + dto.getKakaoAccessToken());

    HttpEntity<String> requestEntity = new HttpEntity<>(headers);
    ResponseEntity<String> responseEntity = restTemplate.exchange(reqURL, HttpMethod.POST, requestEntity, String.class);
    System.out.printf(responseEntity.getBody());

    String email, profileImage = "";
    long kakaoId = 0;

    if (!responseEntity.getStatusCode().is2xxSuccessful()) {
      throw new CustomException(ErrorCode.INVALID_KAKAO_VALUE);
    }

    JsonElement jsonElement = JsonParser.parseString(Objects.requireNonNull(responseEntity.getBody()))
            .getAsJsonObject();

    kakaoId = jsonElement.getAsJsonObject().get("id").getAsLong();

    boolean hasEmail = jsonElement.getAsJsonObject().get("kakao_account").getAsJsonObject().get("has_email")
            .getAsBoolean();
    if (!hasEmail) {
      throw new CustomException(ErrorCode.EMAIL_NOT_FOUND);
    }
    email = jsonElement.getAsJsonObject().get("kakao_account").getAsJsonObject().get("email").getAsString();

    boolean hasProfileImage = jsonElement.getAsJsonObject()
            .getAsJsonObject("kakao_account")
            .getAsJsonObject("profile")
            .has("has_profile_image") && jsonElement.getAsJsonObject()
            .getAsJsonObject("kakao_account")
            .getAsJsonObject("profile")
            .get("has_profile_image")
            .getAsBoolean();

    profileImage = jsonElement.getAsJsonObject()
            .getAsJsonObject("kakao_account")
            .getAsJsonObject("profile")
            .get("profile_image_url")
            .getAsString();


    Optional<User> optionalUser = userRepository.findByEmail(email);
    User user;
    if (optionalUser.isEmpty()) {
      user = userRepository.save(User.builder()
              .email(email)
              .profile(profileImage)
              .socialId(String.valueOf(kakaoId))
              .build());
    } else {
      user = optionalUser.get();

      if (!Objects.equals(user.getSocialId(), String.valueOf(kakaoId))) {
        throw new CustomException(ErrorCode.DUPLICATED_EMAIL);
      }
    }

    TokenDTO.ServiceToken tokenDTO = tokenProvider.createToken(email);

    Long expireTime = tokenProvider.getExpiration(tokenDTO.getRefreshToken());

    redisTemplate.opsForValue().set(tokenDTO.getRefreshToken(), "refreshToken", expireTime, TimeUnit.MILLISECONDS);

    return tokenDTO;
  }

  public TokenDTO.ServiceToken refresh(HttpServletRequest request, TokenDTO.ServiceToken dto) {
    String refreshToken = dto.getRefreshToken();

    String isValidate = (String) redisTemplate.opsForValue().get(refreshToken);
    if (ObjectUtils.isEmpty(isValidate)) {
      throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
    }
    return tokenProvider.createAccessTokenByRefreshToken(request, refreshToken);
  }

  public void logout(HttpServletRequest request, @RequestBody TokenDTO.ServiceToken dto, Principal principal) {
    String accessToken = tokenProvider.resolveToken(request);

    Long expireTime = tokenProvider.getExpiration(accessToken);

    redisTemplate.opsForValue().set(accessToken, "logout", expireTime, TimeUnit.MILLISECONDS);

    redisTemplate.delete(dto.getRefreshToken());
  }

}
