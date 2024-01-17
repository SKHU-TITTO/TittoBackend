package com.example.titto_backend.sercive.matchingBoard;

import com.example.titto_backend.domain.MatchingBoard.MatchingPost;
import com.example.titto_backend.domain.User;
import com.example.titto_backend.dto.request.MatchingPostCreateRequestDto;
import com.example.titto_backend.dto.request.MatchingPostUpdateRequestDto;
import com.example.titto_backend.dto.response.MatchingPostCreateResponseDto;
import com.example.titto_backend.dto.response.MatchingPostDeleteResponseDto;
import com.example.titto_backend.dto.response.MatchingPostResponseDto;
import com.example.titto_backend.dto.response.MatchingPostUpdateResponseDto;
import com.example.titto_backend.repository.MatchingBoard.MatchingPostRepository;
import com.example.titto_backend.repository.MatchingBoard.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class MatchingPostService {
    private final MatchingPostRepository matchingPostRepository;
    private final UserRepository userRepository;

    //게시물 작성
    @Transactional
    public MatchingPostCreateResponseDto createMatchingPost( Principal principal, MatchingPostCreateRequestDto matchingPostCreateRequestDto) {
        Long userId = Long.valueOf(principal.getName());
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 사용자입니다"));

        MatchingPost matchingPost = matchingPostCreateRequestDto.toEntity();
        matchingPostRepository.save(matchingPost);

        return MatchingPostCreateResponseDto.of(
                matchingPost.getMatchingPostId(),
                user.getNickname(),
                String.valueOf(matchingPost.getCategory()),
                String.valueOf(matchingPost.getStatus()),
                matchingPost.getTitle(),
                matchingPost.getContent(),
                matchingPost.getViewCount(),
                matchingPost.getReview_count(),
                matchingPost.getCreateDate()
        );
    }
    // 게시물 조회
    public MatchingPostResponseDto getMatchingPostByMatchingPostId(Long matchingPostId) {
        MatchingPost matchingPost = matchingPostRepository.findById(matchingPostId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 게시물입니다"));
        return MatchingPostResponseDto.of(
                matchingPost.getMatchingPostId(),
                matchingPost.getUser().getNickname(),
                matchingPost.getCategory(),
                matchingPost.getStatus(),
                matchingPost.getTitle(),
                matchingPost.getContent(),
                matchingPost.getViewCount(),
                matchingPost.getReview_count(),
                matchingPost.getUpdateDate()
        );
    }
    // 게시물 삭제
    @Transactional
    public MatchingPostDeleteResponseDto deleteMatchingPostByMatchingPostId(Long matchingPostId) {
        MatchingPost matchingPost = matchingPostRepository.findById(matchingPostId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 게시물입니다"));
        matchingPostRepository.delete(matchingPost);
        return MatchingPostDeleteResponseDto.of(matchingPostId);
    }
    // 게시물 수정
    @Transactional
    public MatchingPostUpdateResponseDto updateMatchingPost(Long matchingPostId, Principal principal, MatchingPostUpdateRequestDto matchingPostUpdateRequestDto) {
        MatchingPost matchingPost = matchingPostRepository.findById(matchingPostId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 게시물입니다"));

        Long userId = Long.valueOf(principal.getName());
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 사용자입니다"));

        if (user.getNickname().equals(matchingPost.getUser().getNickname())) {
            return MatchingPostUpdateResponseDto.of(
                    matchingPost.getMatchingPostId(),
                    user.getNickname(),
                    String.valueOf(matchingPost.getCategory()),
                    String.valueOf(matchingPost.getStatus()),
                    matchingPost.getTitle(),
                    matchingPost.getContent(),
                    matchingPost.getViewCount(),
                    matchingPost.getReview_count(),
                    matchingPost.getUpdateDate()
            );
        }
        else throw new AuthorizationServiceException("잘못된 접근입니다");
    }
    // 게시글 조회수 연산
    private void countViews(MatchingPost matchingPost, HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = null;
        Cookie[] cookies = request.getCookies();
        if (matchingPost != null) {
            if (cookies != null) {
                for (Cookie oldCookie : cookies) {
                    if (oldCookie.getName().equals("postVies")) {
                        cookie = oldCookie;
                        break;
                    }
                }
            }
            if (cookie != null) {
                if (!cookie.getValue().contains("POST[" + matchingPost.getMatchingPostId() + "]")) {
                    matchingPost.updateViewCount();
                    matchingPostRepository.save(matchingPost);
                    cookie.setValue(cookie.getValue() + "POST[" + matchingPost.getMatchingPostId() + "]");
                }
            } else {
                matchingPost.updateViewCount();
                matchingPostRepository.save(matchingPost);
                cookie = new Cookie("postViews", "POST[" + matchingPost.getMatchingPostId() + "]");
            }
            response.addCookie(setCookieValue(cookie));
        }
    }
    // 쿠키 설정
    private Cookie setCookieValue(Cookie cookie) {
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * 24);
        return cookie;
    }
}


