package com.example.titto_backend.matchingBoard.service.matchingBoardReview;

import com.example.titto_backend.auth.domain.User;
import com.example.titto_backend.auth.repository.UserRepository;
import com.example.titto_backend.common.exception.CustomException;
import com.example.titto_backend.common.exception.ErrorCode;
import com.example.titto_backend.matchingBoard.domain.matchingBoard.MatchingPost;
import com.example.titto_backend.matchingBoard.domain.review.MatchingPostReview;
import com.example.titto_backend.matchingBoard.dto.request.matchingPostReviewRequest.MatchingPostReviewCreateRequestDto;
import com.example.titto_backend.matchingBoard.dto.request.matchingPostReviewRequest.MatchingPostReviewDeleteRequestDto;
import com.example.titto_backend.matchingBoard.dto.request.matchingPostReviewRequest.MatchingPostReviewUpdateRequestDto;
import com.example.titto_backend.matchingBoard.dto.response.matchingPostReviewResponse.MatchingPostReviewCreateResponseDto;
import com.example.titto_backend.matchingBoard.dto.response.matchingPostReviewResponse.MatchingPostReviewDeleteResponseDto;
import com.example.titto_backend.matchingBoard.dto.response.matchingPostReviewResponse.MatchingPostReviewResponseDto;
import com.example.titto_backend.matchingBoard.dto.response.matchingPostReviewResponse.MatchingPostReviewUpdateResponseDto;
import com.example.titto_backend.matchingBoard.repository.matchingBoard.MatchingPostRepository;
import com.example.titto_backend.matchingBoard.repository.review.MatchingPostReviewRepository;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MatchingPostReviewService {

    private final MatchingPostReviewRepository matchingPostReviewRepository;
    private final UserRepository userRepository;
    private final MatchingPostRepository matchingPostRepository;

    @Transactional
    public MatchingPostReviewCreateResponseDto createReview(Principal principal,
                                                            MatchingPostReviewCreateRequestDto matchingPostReviewCreateRequestDto) {
        User user = getCurrentUser(principal);

        MatchingPost matchingPost = matchingPostRepository.findById(matchingPostReviewCreateRequestDto.getPostId())
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        matchingPost.increaseReviewCount();

        MatchingPostReview matchingPostReview = MatchingPostReview.builder()
                .matchingPost(matchingPost)
                .reviewAuthor(user)
                .content(matchingPostReviewCreateRequestDto.getContent())
                .build();
        return new MatchingPostReviewCreateResponseDto(matchingPostReviewRepository.save(matchingPostReview));
    }

    public List<MatchingPostReviewResponseDto> getAllMatchingBoardReviewsByPostId(Long postId) {
        MatchingPost matchingPost = matchingPostRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        List<MatchingPostReview> matchingPostReviews = matchingPostReviewRepository.findAllByMatchingPost(matchingPost);

        return matchingPostReviews.stream()
                .map(MatchingPostReviewResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public MatchingPostReviewUpdateResponseDto updateReview(Principal principal,
                                                            MatchingPostReviewUpdateRequestDto matchingPostReviewUpdateRequestDto) {
        User user = getCurrentUser(principal);
        validateMatchingPostReviewAuthorIsLoggedInUser(matchingPostReviewUpdateRequestDto.getReviewId(), user);

        MatchingPostReview matchingPostReview = MatchingPostReview.builder()
                .review_id(matchingPostReviewUpdateRequestDto.getReviewId())
                .matchingPost(matchingPostRepository.findById(matchingPostReviewUpdateRequestDto.getPostId())
                        .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND)))
                .reviewAuthor(user)
                .content(matchingPostReviewUpdateRequestDto.getContent())
                .build();
        return new MatchingPostReviewUpdateResponseDto(matchingPostReviewRepository.save(matchingPostReview));
    }

    @Transactional
    public MatchingPostReviewDeleteResponseDto deleteReviewByReviewId(
            MatchingPostReviewDeleteRequestDto matchingPostReviewDeleteRequestDto,
            Principal principal) {
        User user = getCurrentUser(principal);
        validateMatchingPostReviewAuthorIsLoggedInUser(matchingPostReviewDeleteRequestDto.getReviewId(), user);

        MatchingPost matchingPost = matchingPostRepository.findById(matchingPostReviewDeleteRequestDto.getPostId())
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        matchingPost.decreaseReviewCount();

        MatchingPostReview matchingPostReview = matchingPostReviewRepository.findById(
                matchingPostReviewDeleteRequestDto.getReviewId()).orElseThrow(
                () -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));
        matchingPostReviewRepository.delete(matchingPostReview);

        return MatchingPostReviewDeleteResponseDto.of(matchingPostReviewDeleteRequestDto.getReviewId());
    }

    private User getCurrentUser(Principal principal) {
        String userEmail = principal.getName();
        return userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    private void validateMatchingPostReviewAuthorIsLoggedInUser(Long reviewId, User user) {
        MatchingPostReview matchingPostReview = matchingPostReviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));
        if (!matchingPostReview.getReviewAuthor().equals(user)) {
            throw new CustomException(ErrorCode.MISMATCH_AUTHOR);
        }
    }

}
