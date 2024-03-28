package com.example.titto_backend.auth.service;

import com.example.titto_backend.auth.domain.SocialType;
import com.example.titto_backend.auth.domain.User;
import com.example.titto_backend.auth.dto.response.TokenDTO;
import com.example.titto_backend.auth.dto.request.UserDTO;
import com.example.titto_backend.auth.jwt.TokenProvider;
import com.example.titto_backend.auth.repository.UserRepository;
import com.example.titto_backend.common.exception.CustomException;
import com.example.titto_backend.common.exception.ErrorCode;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class OAuthNaverService {

    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final RedisTemplate<String, Object> redisTemplate;

    @Value("${oauth.naver.client-id}")
    private String NAVER_CLIENT_ID;

    @Value("${oauth.naver.client-secret}")
    private String NAVER_CLIENT_SECRET;

    @Value("${oauth.naver.redirect-uri}")
    private String NAVER_REDIRECT_URI;

    public TokenDTO.NaverToken getToken(String code, String state) {
        RestTemplate restTemplate = new RestTemplate();

        String reqURL = "https://nid.naver.com/oauth2.0/token";

        MultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("grant_type", "authorization_code");
        requestParams.add("client_id", NAVER_CLIENT_ID);
        requestParams.add("client_secret", NAVER_CLIENT_SECRET);
        requestParams.add("redirect_uri", NAVER_REDIRECT_URI);
        requestParams.add("code", code);
        requestParams.add("state", state);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestParams, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(reqURL, HttpMethod.POST, requestEntity,
                String.class);

        if (!responseEntity.getStatusCode().is2xxSuccessful()) {
            throw new CustomException(ErrorCode.INVALID_NAVER_VALUE);
        }

        JsonElement jsonElement = JsonParser.parseString(Objects.requireNonNull(responseEntity.getBody()))
                .getAsJsonObject();

        String accessToken = jsonElement.getAsJsonObject().get("access_token").getAsString();
        String refreshToken = jsonElement.getAsJsonObject().get("refresh_token").getAsString();

        return new TokenDTO.NaverToken(accessToken, refreshToken);
    }

    @Transactional
    public TokenDTO.ServiceToken joinAndLogin(UserDTO.LoginRequest dto) {
        RestTemplate restTemplate = new RestTemplate();
        String reqURL = "https://openapi.naver.com/v1/nid/me";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        headers.set("Authorization", "Bearer " + dto.getNaverAccessToken());

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(reqURL, HttpMethod.POST, requestEntity,
                String.class);

        String email = "";
        String profileImage = "";
        String naverId = "";

        if (!responseEntity.getStatusCode().is2xxSuccessful()) {
            throw new CustomException(ErrorCode.INVALID_NAVER_VALUE);
        }

        JsonElement jsonElement = JsonParser.parseString(Objects.requireNonNull(responseEntity.getBody()))
                .getAsJsonObject()
                .getAsJsonObject("response");

        email = jsonElement.getAsJsonObject().get("email").getAsString();
        profileImage = jsonElement.getAsJsonObject().get("profile_image").getAsString();
        naverId = jsonElement.getAsJsonObject().get("id").getAsString();

        Optional<User> optionalUser = userRepository.findByEmail(email);
        User user;
        if (optionalUser.isEmpty()) {
            user = userRepository.save(User.builder()
                    .email(email)
                    .profile(profileImage)
                    .socialId(String.valueOf(naverId))
                    .socialType(SocialType.NAVER)
                    .build());
        } else {
            user = optionalUser.get();

            if (!user.getSocialId().equals(naverId)) {
                throw new CustomException(ErrorCode.DUPLICATED_EMAIL);
            }
        }

        TokenDTO.ServiceToken tokenDTO = tokenProvider.createToken(email);

        Long expireTime = tokenProvider.getExpiration(tokenDTO.getRefreshToken());

        redisTemplate.opsForValue().set(tokenDTO.getRefreshToken(), "refreshToken", expireTime, TimeUnit.MILLISECONDS);

        return tokenDTO;
    }
}
