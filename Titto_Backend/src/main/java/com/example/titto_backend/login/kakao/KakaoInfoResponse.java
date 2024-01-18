package com.example.titto_backend.login.kakao;

import com.example.titto_backend.login.domain.oauth.OAuthInfoResponse;
import com.example.titto_backend.login.domain.oauth.OAuthProvider;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoInfoResponse implements OAuthInfoResponse {

  @JsonProperty("kakao_account")
  private KakaoAccount kakaoAccount;

  @Getter
  @JsonIgnoreProperties(ignoreUnknown = true)
  static class KakaoAccount {
    private String email;
    private KakaoProfile profile;
  }

  @Getter
  @JsonIgnoreProperties(ignoreUnknown = true)
  static class KakaoProfile {
    @JsonProperty("profile_image_url")
    private String profileImageUrl;
  }


  @Override
  public String getEmail() {
    return kakaoAccount.getEmail();
  }

  @Override
  public String getProfileImage() {
    return kakaoAccount.profile.getProfileImageUrl();
  }

  @Override
  public OAuthProvider getOAuthProvider() {
    return OAuthProvider.KAKAO;
  }
}
