package com.example.titto_backend.matchingBoard.service.matchingBoard;

import com.example.titto_backend.auth.domain.User;
import com.example.titto_backend.auth.repository.UserRepository;
import com.example.titto_backend.matchingBoard.domain.matchingBoard.MatchingPost;
import com.example.titto_backend.matchingBoard.dto.request.MatchingPostRequest.MatchingPostCreateRequestDto;
import com.example.titto_backend.matchingBoard.dto.request.MatchingPostRequest.MatchingPostUpdateRequestDto;
import com.example.titto_backend.matchingBoard.dto.response.matchingPostResponse.MatchingPostCreateResponseDto;
import com.example.titto_backend.matchingBoard.dto.response.matchingPostResponse.MatchingPostDeleteResponseDto;
import com.example.titto_backend.matchingBoard.dto.response.matchingPostResponse.MatchingPostResponseDto;
import com.example.titto_backend.matchingBoard.dto.response.matchingPostResponse.MatchingPostUpdateResponseDto;
import com.example.titto_backend.matchingBoard.repository.matchingBoard.MatchingPostRepository;
import com.example.titto_backend.matchingBoard.repository.review.MatchingPostReviewRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class MatchingPostService {
    private final MatchingPostRepository matchingPostRepository;
    private final UserRepository userRepository;
    private final MatchingPostReviewRepository matchingPostReviewRepository;

    //게시물 작성
    @Transactional
    public MatchingPostCreateResponseDto createMatchingPost(Principal principal, MatchingPostCreateRequestDto matchingPostCreateRequestDto) {
        Long userId = Long.valueOf(principal.getName());
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 사용자입니다"));

        MatchingPost matchingPost = matchingPostCreateRequestDto.toEntity();
        matchingPostRepository.save(matchingPost);
        Integer reviewCount = matchingPostReviewRepository.findAllByMatchingPost(matchingPost).size();  // 댓글 수

        return MatchingPostCreateResponseDto.of(matchingPost, reviewCount);
    }
    // 게시물 조회
    @Transactional(readOnly = true)
    public MatchingPostResponseDto getMatchingPostByMatchingPostId(Long matchingPostId, HttpServletRequest request, HttpServletResponse response) {
        MatchingPost matchingPost = matchingPostRepository.findById(matchingPostId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 게시물입니다"));

        Integer reviewCount = matchingPostReviewRepository.findAllByMatchingPost(matchingPost).size();  // 댓글 수
        // 조회수 업데이트
        countViews(matchingPost, request, response);
        matchingPostRepository.save(matchingPost);  // 업데이트된 조회수를 저장합니다.

        return MatchingPostResponseDto.of(matchingPost,reviewCount);
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

        Integer reviewCount = matchingPostReviewRepository.findAllByMatchingPost(matchingPost).size();  // 댓글 수

        if (user.getNickname().equals(matchingPost.getUser().getNickname())) {
            return MatchingPostUpdateResponseDto.of(
                    matchingPost,
                    reviewCount);
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
                    if (oldCookie.getName().equals("postViews")) {
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


