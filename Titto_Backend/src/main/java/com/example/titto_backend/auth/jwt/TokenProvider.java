package com.example.titto_backend.auth.jwt;

import com.example.titto_backend.auth.dto.TokenDTO;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;


import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TokenProvider {

  private final Key key;
  private final long accessTokenValidityTime;
  private final long refreshTokenValidityTime;

  public TokenProvider(@Value("${jwt.secret}") String secretKey,
                       @Value("${jwt.access-token-validity-in-milliseconds}") long accessTokenValidityTime,
                       @Value("${jwt.refresh-token-validity-in-milliseconds}") long refreshTokenValidityTime) {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    this.key = Keys.hmacShaKeyFor(keyBytes);
    this.accessTokenValidityTime = accessTokenValidityTime;
    this.refreshTokenValidityTime = refreshTokenValidityTime;
  }

  public TokenDTO.ServiceToken createToken(String email) {

    long now = (new Date()).getTime();

    Date tokenExpiredTime = new Date(now + accessTokenValidityTime);

    String accessToken = Jwts.builder()
            .setSubject(email)
            .claim("auth", "ROLE_USER")
            .setExpiration(tokenExpiredTime)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();

    tokenExpiredTime = new Date(now + refreshTokenValidityTime);

    String refreshToken = Jwts.builder()
            .setExpiration(tokenExpiredTime)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();

    return TokenDTO.ServiceToken.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build();
  }

  public TokenDTO.ServiceToken createAccessTokenByRefreshToken(HttpServletRequest request, String refreshToken) {
    String[] chunks = resolveToken(request).split("\\.");

    Base64.Decoder decoder = Base64.getUrlDecoder();
    String payload = new String(decoder.decode(chunks[1]));
    String name = payload.split("\"")[3];

    long now = (new Date()).getTime();

    Date tokenExpiredTime = new Date(now + accessTokenValidityTime);

    // AccessToken 생성
    String accessToken = Jwts.builder()
            .setSubject(name)
            .claim("auth", "ROLE_USER")
            .setExpiration(tokenExpiredTime)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();

    return TokenDTO.ServiceToken.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build();
  }

  public Authentication getAuthentication(String accessToken) {
    // AccessToken <- Claims 추출
    Claims claims = parseClaims(accessToken);

    // 권한 정보가 담겨있지 않은 토큰을 받았을 경우
    if (claims.get("auth") == null) {
      throw new IllegalArgumentException("권한 정보가 없는 토큰입니다.");
    }

    Collection<? extends GrantedAuthority> authorities =
            Arrays.stream(claims.get("auth").toString().split(","))
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

    UserDetails principal = new User(claims.getSubject(), "", authorities);
    return new UsernamePasswordAuthenticationToken(principal, "", authorities);
  }

  public String resolveToken(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");

    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }
    return null;
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
      return true;
    }
    catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
      log.info("Invalid JWT Token", e);
    } catch (ExpiredJwtException e) { // accessToken 만료된 경우 refreshToken 검증
      log.info("Expired JWT Token", e);
    } catch (UnsupportedJwtException e) {
      log.info("Unsupported JWT Token", e);
    } catch (IllegalArgumentException e) {
      log.info("JWT claims string is empty.", e);
    }
    return false;
  }

  private Claims parseClaims(String accessToken) {
    try {
      return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
    } catch (ExpiredJwtException e) { // 기한 만료된 토큰
      return e.getClaims();
    }
  }

  public Long getExpiration(String accessToken) {
    Date expiration = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody().getExpiration();
    Long now = new Date().getTime();
    return (expiration.getTime() - now);
  }
}