package com.example.titto_backend.matchingBoard.service.matchingBoard;

import com.example.titto_backend.matchingBoard.domain.matchingBoard.MatchingPost;
import com.example.titto_backend.matchingBoard.dto.request.MatchingPostRequest.MatchingPostPagingRequestDto;
import com.example.titto_backend.matchingBoard.dto.response.matchingPostResponse.MatchingPostPagingResponseDto;
import com.example.titto_backend.matchingBoard.repository.matchingBoard.MatchingPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MatchingBoardService {
    private final MatchingPostRepository matchingPostRepository;

    @Transactional(readOnly = true)
    public MatchingPostPagingResponseDto findAllPosts(MatchingPostPagingRequestDto matchingPostPagingRequestDto) {
        Sort sort = Sort.by(Sort.Direction.ASC, "matchingPostId");
        Pageable pageable = PageRequest.of(matchingPostPagingRequestDto.getPage(), 10, sort); // 페이지 크기를 고정값인 10으로 설정
        Page<MatchingPost> matchingPosts = matchingPostRepository.findAll(pageable);
        return MatchingPostPagingResponseDto.from(matchingPosts);
    }
}

