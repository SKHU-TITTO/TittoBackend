package com.example.titto_backend.matchingBoard.service.matchingBoard;

import com.example.titto_backend.auth.domain.User;
import com.example.titto_backend.auth.repository.UserRepository;
import com.example.titto_backend.matchingBoard.domain.matchingBoard.Category;
import com.example.titto_backend.matchingBoard.domain.matchingBoard.MatchingPost;
import com.example.titto_backend.matchingBoard.domain.matchingBoard.Status;
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
    public MatchingPostCreateResponseDto createMatchingPost(Principal principal,
                                                            MatchingPostCreateRequestDto matchingPostCreateRequestDto) {
        String userEmail = principal.getName();
        User user = userRepository.findByEmail(userEmail).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 사용자입니다"));

        MatchingPost matchingPost = matchingPostCreateRequestDto.toEntity(user);
        matchingPostRepository.save(matchingPost);

        return MatchingPostCreateResponseDto.of(matchingPost);
    }

    // 게시물 조회
    public MatchingPostResponseDto getMatchingPostByMatchingPostId(Long matchingPostId, HttpServletRequest request,
                                                                   HttpServletResponse response) {
        MatchingPost matchingPost = matchingPostRepository.findById(matchingPostId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 게시물입니다"));

        Integer reviewCount = matchingPostReviewRepository.findAllByMatchingPost(matchingPost).size();  // 댓글 수
        // 조회수 업데이트
        countViews(matchingPost, request, response);
        matchingPostRepository.save(matchingPost);  // 업데이트된 조회수를 저장합니다.

        return MatchingPostResponseDto.of(matchingPost, reviewCount);
    }

    // 게시물 삭제
    @Transactional
    public MatchingPostDeleteResponseDto deleteMatchingPostByMatchingPostId(Long matchingPostId) {
        MatchingPost matchingPost = matchingPostRepository.findById(matchingPostId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 게시물입니다"));
        matchingPostReviewRepository.deleteAllByMatchingPost(matchingPost);
        matchingPostRepository.delete(matchingPost);
        return MatchingPostDeleteResponseDto.of(matchingPostId);
    }

    // 게시물 수정
    @Transactional
    public MatchingPostUpdateResponseDto updateMatchingPost(Long matchingPostId, Principal principal,
                                                            MatchingPostUpdateRequestDto matchingPostUpdateRequestDto) {
        MatchingPost matchingPost = matchingPostRepository.findById(matchingPostId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 게시물입니다"));

        String userEmail = principal.getName();
        User user = userRepository.findByEmail(userEmail).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 사용자입니다"));

        Integer reviewCount = matchingPostReviewRepository.findAllByMatchingPost(matchingPost).size();  // 댓글 수

        if (user.getNickname().equals(matchingPost.getUser().getNickname())) {
            // 게시물 내용 수정
            matchingPost.update(
                    Category.valueOf(matchingPostUpdateRequestDto.getCategory()),
                    matchingPostUpdateRequestDto.getTitle(),
                    matchingPostUpdateRequestDto.getContent(),
                    Status.valueOf(matchingPostUpdateRequestDto.getStatus())
            );

            return MatchingPostUpdateResponseDto.of(
                    matchingPost,
                    reviewCount);
        } else {
            throw new AuthorizationServiceException("잘못된 접근입니다");
        }
    }

    // 게시글 조회수 연산
    private void countViews(MatchingPost matchingPost, HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = null;
        Cookie[] cookies = request.getCookies();
        if (matchingPost != null) {
            String postId = "POST[" + matchingPost.getMatchingPostId() + "]";
            if (cookies != null) {
                // 쿠키 중에서 "postViews" 쿠키를 찾습니다.
                for (Cookie oldCookie : cookies) {
                    if (oldCookie.getName().equals("postViews")) {
                        cookie = oldCookie;
                        break;
                    }
                }
            }
            if (cookie != null) {
                // "postViews" 쿠키가 있는 경우
                if (!cookie.getValue().contains(postId)) {
                    // 쿠키의 값에 현재 게시물의 ID가 없다면 조회수를 증가시키고 쿠키를 업데이트합니다.
                    matchingPost.updateViewCount();
                    cookie.setValue(cookie.getValue() + postId);
                }
            } else {
                // "postViews" 쿠키가 없는 경우 쿠키를 생성하고 조회수를 증가시킵니다.
                matchingPost.updateViewCount();
                cookie = new Cookie("postViews", postId);
            }
            response.addCookie(setCookieValue(cookie));  // 쿠키를 응답에 추가합니다.
            matchingPostRepository.save(matchingPost);  // 게시물을 저장하여 조회수를 업데이트합니다.
        }
    }

    // 쿠키 설정
    private Cookie setCookieValue(Cookie cookie) {
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * 24);
        return cookie;
    }
}


