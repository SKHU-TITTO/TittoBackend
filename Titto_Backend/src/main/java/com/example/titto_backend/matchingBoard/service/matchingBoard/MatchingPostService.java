package com.example.titto_backend.matchingBoard.service.matchingBoard;

import com.example.titto_backend.auth.domain.User;
import com.example.titto_backend.auth.repository.UserRepository;
import com.example.titto_backend.common.exception.CustomException;
import com.example.titto_backend.common.exception.ErrorCode;
import com.example.titto_backend.common.util.RedisUtil;
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
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MatchingPostService {
    private final MatchingPostRepository matchingPostRepository;
    private final UserRepository userRepository;
    private final MatchingPostReviewRepository matchingPostReviewRepository;
    private final RedisUtil redisUtil;

    // 게시물 작성
    @Transactional
    public MatchingPostCreateResponseDto createMatchingPost(Principal principal,
                                                            MatchingPostCreateRequestDto matchingPostCreateRequestDto) {
        User user = getCurrentUser(principal);
        MatchingPost matchingPost = matchingPostCreateRequestDto.toEntity(user);
        matchingPostRepository.save(matchingPost);
        return MatchingPostCreateResponseDto.of(matchingPost);
    }

    // 게시물 조회
    @Transactional
    public MatchingPostResponseDto findByMatchingPostId(Principal principal, Long matchingPostId) {
        User user = getCurrentUser(principal);
        MatchingPost matchingPost = findMatchingPostById(matchingPostId);
        countViews(user, matchingPost);
        return MatchingPostResponseDto.of(matchingPost);
    }

    // 게시물 삭제
    @Transactional
    public MatchingPostDeleteResponseDto deleteMatchingPostByMatchingPostId(Long matchingPostId, Principal principal) {
        User user = getCurrentUser(principal);
        validateMatchingPostAuthorIsLoggedInUser(matchingPostId, user);

        MatchingPost matchingPost = findMatchingPostById(matchingPostId);

        matchingPostReviewRepository.deleteAllByMatchingPost(matchingPost);
        matchingPostRepository.delete(matchingPost);
        return MatchingPostDeleteResponseDto.of(matchingPostId);
    }

    // 게시물 수정
    @Transactional
    public MatchingPostUpdateResponseDto updateMatchingPost(Long matchingPostId, Principal principal,
                                                            MatchingPostUpdateRequestDto matchingPostUpdateRequestDto) {
        User user = getCurrentUser(principal);
        validateMatchingPostAuthorIsLoggedInUser(matchingPostId, user);
        MatchingPost matchingPost = findMatchingPostById(matchingPostId);

        // 게시물 내용 수정
        matchingPost.update(
                Category.valueOf(matchingPostUpdateRequestDto.getCategory()),
                matchingPostUpdateRequestDto.getTitle(),
                matchingPostUpdateRequestDto.getContent(),
                Status.valueOf(matchingPostUpdateRequestDto.getStatus())
        );
        return MatchingPostUpdateResponseDto.of(matchingPost);
    }

    @Transactional
    public void countViews(User user, MatchingPost matchingPost) {
        String key = String.format("matchingPostViewCount:%d:%d", user.getId(), matchingPost.getMatchingPostId());
        String viewCount = redisUtil.getData(key);

        if (viewCount == null) {
            redisUtil.setDateExpire(key, "1", calculateTimeUntilMidnight());
            matchingPost.updateViewCount();
        } else {
            int count = Integer.parseInt(viewCount);
            if (count < 1) {
                count++;
                redisUtil.setDateExpire(key, String.valueOf(count), calculateTimeUntilMidnight());
                matchingPost.updateViewCount();
            }
        }
    }

    public static long calculateTimeUntilMidnight() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime midnight = now.truncatedTo(ChronoUnit.DAYS).plusDays(1);
        return ChronoUnit.SECONDS.between(now, midnight);
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

    private void validateMatchingPostAuthorIsLoggedInUser(Long postId, User user) {
        MatchingPost matchingPost = matchingPostRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));
        if (!matchingPost.getUser().equals(user)) {
            throw new CustomException(ErrorCode.MISMATCH_AUTHOR);
        }
    }
}