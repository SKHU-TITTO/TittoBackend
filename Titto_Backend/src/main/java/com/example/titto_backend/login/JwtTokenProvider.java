package com.example.titto_backend.login;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

  private final Key key;

  public JwtTokenProvider(@Value("${jwt.secret}") String secretKey) {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    this.key = Keys.hmacShaKeyFor(keyBytes);
  }

  public String generate(String subject, Date expiredAt) {
    return Jwts.builder()
            .setSubject(subject)
            .setExpiration(expiredAt)
            .signWith(key, SignatureAlgorithm.HS512)
            .compact();
  }

  public String extractSubject(String accessToken) {
    Claims claims = parseClaims(accessToken);
    return claims.getSubject();
  }

  public Claims parseClaims(String accessToken) {
    try {
      return Jwts.parserBuilder()
              .setSigningKey(key)
              .build()
              .parseClaimsJws(accessToken)
              .getBody();
    } catch (ExpiredJwtException e) {
      return e.getClaims();
    }
  }
}