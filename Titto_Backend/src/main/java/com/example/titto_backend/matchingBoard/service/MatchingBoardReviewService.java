package com.example.titto_backend.matchingBoard.service;

import com.example.titto_backend.auth.domain.User;
import com.example.titto_backend.matchingBoard.domain.review.MatchingPostReview;
import com.example.titto_backend.matchingBoard.dto.request.matchingPostReviewRequest.MatchingPostReviewCreateRequestDto;
import com.example.titto_backend.matchingBoard.dto.response.matchingPostReviewResponse.MatchingPostReviewCreateResponseDto;
import com.example.titto_backend.matchingBoard.repository.MatchingBoard.MatchingPostRepository;
import com.example.titto_backend.matchingBoard.repository.review.MatchingPostReviewRepository;
import com.example.titto_backend.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class MatchingBoardReviewService {
    private final MatchingPostReviewRepository matchingPostReviewRepository;
    private final UserRepository userRepository;
    private final MatchingPostRepository matchingPostRepository;

    // 생성
    @Transactional
    public MatchingPostReviewCreateResponseDto createReview(Principal principal,MatchingPostReviewCreateRequestDto matchingPostReviewCreateRequestDto) {
        Long userId = Long.valueOf(principal.getName());
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        MatchingPostReview matchingPostReview = matchingPostReviewCreateRequestDto.toEntity();
        matchingPostReviewRepository.save(matchingPostReview);

        return MatchingPostReviewCreateResponseDto.of(
                matchingPostReview.getReview_id(),
                matchingPostReview.getMatchingPost().getMatchingPostId(),
                matchingPostReview.getReviewAuthor().getNickname(),
                matchingPostReview.getContent(),
                matchingPostReview.getCreateDate()
        );
    }

/*   public List<MatchingPostReviewResponseDto> getAllMatchingBoardReviewsByPostId(Long postId) {
       MatchingPost matchingPost = matchingPostRepository.findById(postId)
               .orElseThrow(() -> new NoSuchElementException("게시물이 존재하지 않습니다."));

     List<MatchingPostReview> matchingPostReviews = matchingPostReviewRepository.findAllById(matchingPost);

   }*/
}
