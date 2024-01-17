package com.example.titto_backend.service.matchingBoard;

import com.example.titto_backend.domain.MatchingBoard.MatchingPost;
import com.example.titto_backend.dto.request.MatchingPostPagingRequestDto;
import com.example.titto_backend.dto.response.MatchingPostPagingResponseDto;
import com.example.titto_backend.repository.MatchingBoard.MatchingPostRepository;
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
        Sort sort = Sort.by(Sort.Direction.fromString(matchingPostPagingRequestDto.getSort()), "matchingPostId");
        Pageable pageable = PageRequest.of(matchingPostPagingRequestDto.getPage(), matchingPostPagingRequestDto.getSize(), sort);
        Page<MatchingPost> matchingPosts = matchingPostRepository.findAll(pageable);
        return new MatchingPostPagingResponseDto(matchingPosts);
    }
}
