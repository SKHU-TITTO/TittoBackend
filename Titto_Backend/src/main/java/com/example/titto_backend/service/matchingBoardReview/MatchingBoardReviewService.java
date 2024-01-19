package com.example.titto_backend.service.matchingBoardReview;

import com.example.titto_backend.domain.User;
import com.example.titto_backend.domain.matchingBoard.MatchingPost;
import com.example.titto_backend.domain.review.MatchingPostReview;
import com.example.titto_backend.dto.request.matchingPostReviewRequest.MatchingPostReviewCreateRequestDto;
import com.example.titto_backend.dto.response.matchingPostReviewResponse.MatchingPostReviewCreateResponseDto;
import com.example.titto_backend.dto.response.matchingPostReviewResponse.MatchingPostReviewResponseDto;
import com.example.titto_backend.repository.MatchingBoard.MatchingPostRepository;
import com.example.titto_backend.repository.MatchingPostReviewRepository;
import com.example.titto_backend.repository.UserRepository;
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
        Long userId = Long.valueOf(principal.getName());
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

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


}
