package com.example.titto_backend.login.naver;

import com.example.titto_backend.login.domain.oauth.OAuthInfoResponse;
import com.example.titto_backend.login.domain.oauth.OAuthProvider;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class NaverInfoResponse implements OAuthInfoResponse {

  @JsonProperty("response")
  private Response response;

  @Getter
  @JsonIgnoreProperties(ignoreUnknown = true)
    static class Response {
        private String email;
        private String profile_image;
    }

  @Override
  public String getEmail() {
    return response.email;
  }

  @Override
  public String getProfileImage() {
    return response.profile_image;
  }

  @Override
  public OAuthProvider getOAuthProvider() {
    return OAuthProvider.NAVER;
  }
}
