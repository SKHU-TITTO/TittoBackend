package com.example.titto_backend.matchingBoard.service.matchingBoard;

import com.example.titto_backend.auth.domain.User;
import com.example.titto_backend.auth.repository.UserRepository;
import com.example.titto_backend.common.exception.CustomException;
import com.example.titto_backend.common.exception.ErrorCode;
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
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        User user = getCurrentUser(principal);
        MatchingPost matchingPost = matchingPostCreateRequestDto.toEntity(user);
        matchingPostRepository.save(matchingPost);

        return MatchingPostCreateResponseDto.of(matchingPost);
    }

    // 게시물 조회
    @Transactional(readOnly = true)
    public MatchingPostResponseDto getMatchingPostByMatchingPostId(Long matchingPostId, HttpServletRequest request,
                                                                   HttpServletResponse response) {
        MatchingPost matchingPost = findMatchingPostById(matchingPostId);
        Integer reviewCount = matchingPostReviewRepository.countByMatchingPost(matchingPost);
        updatePostViews(request, response, matchingPostId);
        return MatchingPostResponseDto.of(matchingPost, reviewCount);
    }

    // 게시물 삭제
    @Transactional
    public MatchingPostDeleteResponseDto deleteMatchingPostByMatchingPostId(Long matchingPostId) {
        MatchingPost matchingPost = findMatchingPostById(matchingPostId);

        matchingPostReviewRepository.deleteAllByMatchingPost(matchingPost);
        matchingPostRepository.delete(matchingPost);
        return MatchingPostDeleteResponseDto.of(matchingPostId);
    }

    // 게시물 수정
    @Transactional
    public MatchingPostUpdateResponseDto updateMatchingPost(Long matchingPostId, Principal principal,
                                                            MatchingPostUpdateRequestDto matchingPostUpdateRequestDto) {
        MatchingPost matchingPost = findMatchingPostById(matchingPostId);

        User user = getCurrentUser(principal);
        Integer reviewCount = matchingPostReviewRepository.countByMatchingPost(matchingPost);

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
            throw new CustomException(ErrorCode.INVALID_ACCESS);
        }
    }

    // 게시글 조회수 연산
    private void increaseViewCount(MatchingPost matchingPost) {
        matchingPost.updateViewCount();
        matchingPostRepository.save(matchingPost);
    }

    private void createOrUpdatePostViewsCookie(HttpServletRequest request, HttpServletResponse response,
                                               MatchingPost matchingPost) {
        Cookie[] cookies = request.getCookies();
        String postId = "POST[" + matchingPost.getMatchingPostId() + "]";
        Cookie postViewsCookie = null;
        if (cookies != null) {
            for (Cookie oldCookie : cookies) {
                if (oldCookie.getName().equals("postViews")) {
                    postViewsCookie = oldCookie;
                    break;
                }
            }
        }
        if (postViewsCookie == null) {
            postViewsCookie = new Cookie("postViews", postId);
        } else {
            // 쿠키가 null이 아닌 경우에도 현재 게시물을 확인하여 포함되어 있지 않으면 값을 업데이트합니다.
            if (!postViewsCookie.getValue().contains(postId)) {
                postViewsCookie.setValue(postViewsCookie.getValue() + postId);
            }
        }
        postViewsCookie.setPath("/");
        postViewsCookie.setMaxAge(60 * 60 * 24);
        response.addCookie(postViewsCookie);
    }


    public void updatePostViews(HttpServletRequest request, HttpServletResponse response, Long matchingPostId) {
        MatchingPost matchingPost = findMatchingPostById(matchingPostId);
        increaseViewCount(matchingPost);
        createOrUpdatePostViewsCookie(request, response, matchingPost);
    }

    private User getCurrentUser(Principal principal) {
        String userEmail = principal.getName();
        return userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    private MatchingPost findMatchingPostById(Long matchingPostId) {
        return matchingPostRepository.findById(matchingPostId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
    }
}


