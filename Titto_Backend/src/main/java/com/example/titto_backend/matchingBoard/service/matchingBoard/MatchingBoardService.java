package com.example.titto_backend.matchingBoard.service.matchingBoard;

import com.example.titto_backend.matchingBoard.domain.matchingBoard.Category;
import com.example.titto_backend.matchingBoard.domain.matchingBoard.MatchingPost;
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

    @Transactional
    public MatchingPostPagingResponseDto findAllPosts(int page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by("createDate").descending());
        Page<MatchingPost> matchingPosts = matchingPostRepository.findAll(pageable);
        return MatchingPostPagingResponseDto.from(matchingPosts);
    }

    @Transactional
    public MatchingPostPagingResponseDto searchByKeyWord(int page,
                                                         String keyword) {

        Pageable pageable = PageRequest.of(page, 10, Sort.by("createDate").descending());
        Page<MatchingPost> matchingPosts = matchingPostRepository.findByTitleContaining(keyword, pageable);
        return MatchingPostPagingResponseDto.from(matchingPosts);
    }

    @Transactional
    public MatchingPostPagingResponseDto findByCategory(int page,
                                                        String category) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by("createDate").descending());
        Page<MatchingPost> matchingPosts = matchingPostRepository.findByCategory(Category.valueOf(category), pageable);
        return MatchingPostPagingResponseDto.from(matchingPosts);
    }
}


