package com.example.titto_backend.service;

import com.example.titto_backend.domain.User;
import com.example.titto_backend.login.AuthTokens;
import com.example.titto_backend.login.AuthTokensGenerator;
import com.example.titto_backend.login.domain.oauth.OAuthInfoResponse;
import com.example.titto_backend.login.domain.oauth.OAuthLoginParams;
import com.example.titto_backend.login.domain.oauth.RequestOAuthInfoService;
import com.example.titto_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuthLoginService {
  private final UserRepository userRepository;
  private final AuthTokensGenerator authTokensGenerator;
  private final RequestOAuthInfoService requestOAuthInfoService;

  public AuthTokens login(OAuthLoginParams params) {
    OAuthInfoResponse oAuthInfoResponse = requestOAuthInfoService.request(params);
    Long userId = findOrCreateUser(oAuthInfoResponse);
    return authTokensGenerator.generate(userId);
  }

  private Long findOrCreateUser(OAuthInfoResponse oAuthInfoResponse) {
    return userRepository.findByEmail(oAuthInfoResponse.getEmail())
            .map(User::getId)
            .orElseGet(() -> newUser(oAuthInfoResponse));
  }

  private Long newUser(OAuthInfoResponse oAuthInfoResponse) {
    User user = User.builder()
            .email(oAuthInfoResponse.getEmail())
            .image_url(oAuthInfoResponse.getProfileImage())
            .oAuthProvider(oAuthInfoResponse.getOAuthProvider())
            .build();

    return userRepository.save(user).getId();
  }
}

