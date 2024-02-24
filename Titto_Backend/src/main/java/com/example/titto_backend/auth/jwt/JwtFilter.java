package com.example.titto_backend.auth.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {

    private final TokenProvider tokenProvider;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        // resolveToken()를 이용해서 토큰을 받아옴
        String token = tokenProvider.resolveToken((HttpServletRequest) request);

        // "/oauth/refresh" 요청은 accessToken이 만료되었을 때 접근하기 때문에 통과시킴
        if (((HttpServletRequest) request).getRequestURI().equals("/oauth/refresh")) {
            chain.doFilter(request, response);
        } else {
            // 토큰이 비어있지 않으면서 유효한 경우
            if (StringUtils.hasText(token) && tokenProvider.validateToken(token)) {
                // Redis에 해당 AccessToken logout 여부를 확인
                String isLogout = (String) redisTemplate.opsForValue().get(token);

                if (ObjectUtils.isEmpty(isLogout)) {
                    // 조건에 만족하면 토큰으로부터 유저 정보를 Authentication 객체에 저장
                    Authentication authentication = tokenProvider.getAuthentication(token);

                    // SecurityContext에 Authentication 객체를 저장 (인증 정보(authentication)를 Spring Security에게 넘김)
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
            chain.doFilter(request, response);
        }
    }
}
