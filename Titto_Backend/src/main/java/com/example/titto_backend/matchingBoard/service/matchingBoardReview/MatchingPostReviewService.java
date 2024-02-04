package com.example.titto_backend.matchingBoard.service.matchingBoardReview;

import com.example.titto_backend.auth.domain.User;
import com.example.titto_backend.auth.repository.UserRepository;
import com.example.titto_backend.matchingBoard.domain.matchingBoard.MatchingPost;
import com.example.titto_backend.matchingBoard.domain.review.MatchingPostReview;
import com.example.titto_backend.matchingBoard.dto.request.matchingPostReviewRequest.MatchingPostReviewCreateRequestDto;
import com.example.titto_backend.matchingBoard.dto.request.matchingPostReviewRequest.MatchingPostReviewUpdateRequestDto;
import com.example.titto_backend.matchingBoard.dto.response.matchingPostReviewResponse.MatchingPostReviewCreateResponseDto;
import com.example.titto_backend.matchingBoard.dto.response.matchingPostReviewResponse.MatchingPostReviewDeleteResponseDto;
import com.example.titto_backend.matchingBoard.dto.response.matchingPostReviewResponse.MatchingPostReviewResponseDto;
import com.example.titto_backend.matchingBoard.dto.response.matchingPostReviewResponse.MatchingPostReviewUpdateResponseDto;
import com.example.titto_backend.matchingBoard.repository.matchingBoard.MatchingPostRepository;
import com.example.titto_backend.matchingBoard.repository.review.MatchingPostReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class MatchingBoardReviewService {
    private final MatchingPostReviewRepository matchingPostReviewRepository;
    private final UserRepository userRepository;
    private final MatchingPostRepository matchingPostRepository;

    // 생성
    @Transactional
    public MatchingPostReviewCreateResponseDto createReview(Principal principal, MatchingPostReviewCreateRequestDto matchingPostReviewCreateRequestDto) {
        String userEmail = principal.getName();
        User user = userRepository.findByEmail(userEmail).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 사용자입니다"));

        MatchingPostReview matchingPostReview = MatchingPostReview.builder()
                .matchingPost(matchingPostRepository.findById(matchingPostReviewCreateRequestDto.getPostId())
                        .orElseThrow(() -> new NoSuchElementException("게시물이 존재하지 않습니다.")))
                .reviewAuthor(user)
                .content(matchingPostReviewCreateRequestDto.getContent())
                .build();
        return new MatchingPostReviewCreateResponseDto(matchingPostReviewRepository.save(matchingPostReview));
    }
    // 조회
    public List<MatchingPostReviewResponseDto> getAllMatchingBoardReviewsByPostId(Long postId) {
        MatchingPost matchingPost = matchingPostRepository.findById(postId)
                .orElseThrow(() -> new NoSuchElementException("게시물이 존재하지 않습니다."));

        List<MatchingPostReview> matchingPostReviews = matchingPostReviewRepository.findAllByMatchingPost(matchingPost);
        return new ArrayList<>();
    }

    // 수정
    @Transactional
    public MatchingPostReviewUpdateResponseDto updateReview(Principal principal, MatchingPostReviewUpdateRequestDto matchingPostReviewUpdateRequestDto) {
        String userEmail = principal.getName();
        User user = userRepository.findByEmail(userEmail).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 사용자입니다"));

        MatchingPostReview matchingPostReview = MatchingPostReview.builder()
                .review_id(matchingPostReviewUpdateRequestDto.getReviewId())
                .matchingPost(matchingPostRepository.findById(matchingPostReviewUpdateRequestDto.getPostId())
                        .orElseThrow(() -> new NoSuchElementException("게시물이 존재하지 않습니다.")))
                .reviewAuthor(user)
                .content(matchingPostReviewUpdateRequestDto.getContent())
                .build();
        return new MatchingPostReviewUpdateResponseDto(matchingPostReviewRepository.save(matchingPostReview));
    }


    // 삭제
    @Transactional
    public MatchingPostReviewDeleteResponseDto deleteReviewByReviewId(Long reviewId) {
        MatchingPostReview matchingPostReview = matchingPostReviewRepository.findById(reviewId).orElseThrow(
                () -> new NoSuchElementException("존재하지 않는 댓글입니다"));
        matchingPostReviewRepository.delete(matchingPostReview);
        return MatchingPostReviewDeleteResponseDto.of(reviewId);
    }
}
