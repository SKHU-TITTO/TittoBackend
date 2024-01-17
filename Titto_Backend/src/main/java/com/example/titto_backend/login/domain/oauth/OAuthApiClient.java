package com.example.titto_backend.login.domain.oauth;

public interface OAuthApiClient {
  OAuthProvider oAuthProvider();

  String requestAccessToken(OAuthLoginParams oAuthLoginParams);

  OAuthInfoResponse requestOauthInfo(String accessToken);
}
