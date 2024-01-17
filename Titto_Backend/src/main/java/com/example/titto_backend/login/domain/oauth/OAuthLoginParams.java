package com.example.titto_backend.login.domain.oauth;

import org.springframework.util.MultiValueMap;

public interface OAuthLoginParams {
  OAuthProvider oAuthProvider();
  MultiValueMap<String, String> makeBody();
}
