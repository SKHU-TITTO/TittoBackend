package com.example.titto_backend.login.naver;

import com.example.titto_backend.login.domain.oauth.OAuthApiClient;
import com.example.titto_backend.login.domain.oauth.OAuthInfoResponse;
import com.example.titto_backend.login.domain.oauth.OAuthLoginParams;
import com.example.titto_backend.login.domain.oauth.OAuthProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class NaverApiClient implements OAuthApiClient {
  private static final String GRANT_TYPE = "authorization_code";

  @Value("${oauth.naver.url.auth}")
  private String authUrl;
  @Value("${oauth.naver.url.api}")
  private String apiUrl;
  @Value("${oauth.naver.client-id}")
  private String clientId;
  @Value("${oauth.naver.secret}")
  private String clientSecret;

  private final RestTemplate restTemplate;

  @Override
  public OAuthProvider oAuthProvider() {
    return OAuthProvider.NAVER;
  }

  @Override
  public String requestAccessToken(OAuthLoginParams oAuthLoginParams) {
    String url = authUrl + "/oauth2.0/token";

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    MultiValueMap<String, String> body = oAuthLoginParams.makeBody();
    body.add("grant_type", GRANT_TYPE);
    body.add("client_id", clientId);
    body.add("client_secret", clientSecret);

    HttpEntity<?> request = new HttpEntity<>(body, headers);

    NaverTokens response = restTemplate.postForObject(url, request, NaverTokens.class);

    assert response != null;
    return response.getAccessToken();
  }

  @Override
  public OAuthInfoResponse requestOauthInfo(String accessToken) {
    String url = apiUrl + "/v1/nid/me";

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    headers.set("Authorization", "Bearer " + accessToken);

    MultiValueMap<String, String> body = new LinkedMultiValueMap<>();

    HttpEntity<?> request = new HttpEntity<>(body, headers);

    return restTemplate.postForObject(url, request, NaverInfoResponse.class);
  }
}
