package com.example.titto_backend.login.kakao;

import com.example.titto_backend.login.domain.oauth.OAuthLoginParams;
import com.example.titto_backend.login.domain.oauth.OAuthProvider;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class KakaoLoginParams implements OAuthLoginParams {
  private String authorizationCode;


  @Override
  public OAuthProvider oAuthProvider() {
    return OAuthProvider.KAKAO;
  }

  @Override
  public MultiValueMap<String, String> makeBody() {
    MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
    body.add("code", authorizationCode);
    return body;
  }
}
