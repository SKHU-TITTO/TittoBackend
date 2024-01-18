package com.example.titto_backend.login.domain.oauth;

public interface OAuthInfoResponse {
  String getEmail(); // 사용자 이메일

  String getProfileImage(); // 사용자 프로필 이미지

  OAuthProvider getOAuthProvider(); // OAuth 제공자
}
