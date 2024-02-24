package com.example.titto_backend.matchingBoard.service.matchingBoard;

import com.example.titto_backend.matchingBoard.domain.matchingBoard.Category;
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
        int page = matchingPostPagingRequestDto.getPage() - 1;

        Sort sort = Sort.by(Sort.Direction.DESC, "matchingPostId");
        Pageable pageable = PageRequest.of(page, 10, sort);
        Page<MatchingPost> matchingPosts = matchingPostRepository.findAll(pageable);
        return MatchingPostPagingResponseDto.from(matchingPosts);
    }

    public MatchingPostPagingResponseDto searchByKeyWord(MatchingPostPagingRequestDto matchingPostPagingRequestDto,
                                                         String keyword) {
        int page = matchingPostPagingRequestDto.getPage() - 1;

        Sort sort = Sort.by(Sort.Direction.DESC, "matchingPostId");
        Pageable pageable = PageRequest.of(page, 10, sort);
        Page<MatchingPost> matchingPosts = matchingPostRepository.findByTitleContaining(keyword, pageable);
        return MatchingPostPagingResponseDto.from(matchingPosts);
    }

    public MatchingPostPagingResponseDto findByCategory(MatchingPostPagingRequestDto matchingPostPagingRequestDto,
                                                        String category) {
        int page = matchingPostPagingRequestDto.getPage() - 1;

        Sort sort = Sort.by(Sort.Direction.DESC, "matchingPostId");
        Pageable pageable = PageRequest.of(page, 10, sort);
        Page<MatchingPost> matchingPosts = matchingPostRepository.findByCategory(Category.valueOf(category), pageable);
        return MatchingPostPagingResponseDto.from(matchingPosts);
    }
}


