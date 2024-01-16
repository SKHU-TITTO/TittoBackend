package com.example.titto_backend.sercive.matchingBoard;

import com.example.titto_backend.domain.MatchingBoard.MatchingPost;
import com.example.titto_backend.domain.User;
import com.example.titto_backend.dto.request.MatchingPostCreateRequestDto;
import com.example.titto_backend.dto.request.MatchingPostUpdateRequestDto;
import com.example.titto_backend.dto.response.MatchingPostCreateResponseDto;
import com.example.titto_backend.dto.response.MatchingPostDeleteResponseDto;
import com.example.titto_backend.dto.response.MatchingPostResponseDto;
import com.example.titto_backend.dto.response.MatchingPostUpdateResponseDto;
import com.example.titto_backend.repository.MatchingBoard.MatchingPostRepository;
import com.example.titto_backend.repository.MatchingBoard.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class MatchingPostService {
    private final MatchingPostRepository matchingPostRepository;
    private final UserRepository userRepository;

    //게시물 작성
    @Transactional
    public MatchingPostCreateResponseDto createMatchingPost( Principal principal, MatchingPostCreateRequestDto matchingPostCreateRequestDto) {
        Long userId = Long.valueOf(principal.getName());
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 사용자입니다"));

        MatchingPost matchingPost = matchingPostCreateRequestDto.toEntity();
        matchingPostRepository.save(matchingPost);

        return MatchingPostCreateResponseDto.of(
                matchingPost.getMatchingPostId(),
                user.getNickname(),
                String.valueOf(matchingPost.getCategory()),
                String.valueOf(matchingPost.getStatus()),
                matchingPost.getTitle(),
                matchingPost.getContent(),
                matchingPost.getViewCount(),
                matchingPost.getCreateDate()
        );
    }
    // 게시물 조회
    public MatchingPostResponseDto getMatchingPostByMatchingPostId(Long matchingPostId) {
        MatchingPost matchingPost = matchingPostRepository.findById(matchingPostId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 게시물입니다"));
        return MatchingPostResponseDto.of(
                matchingPost.getMatchingPostId(),
                matchingPost.getUser().getNickname(),
                matchingPost.getCategory(),
                matchingPost.getStatus(),
                matchingPost.getTitle(),
                matchingPost.getContent(),
                matchingPost.getViewCount(),
                matchingPost.getCreateDate()
        );
    }
    // 게시물 삭제
    @Transactional
    public MatchingPostDeleteResponseDto deleteMatchingPostByMatchingPostId(Long matchingPostId) {
        MatchingPost matchingPost = matchingPostRepository.findById(matchingPostId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 게시물입니다"));
        matchingPostRepository.delete(matchingPost);
        return MatchingPostDeleteResponseDto.of(matchingPostId);
    }
    // 게시물 수정
    @Transactional
    public MatchingPostUpdateResponseDto updateMatchingPost(Long matchingPostId, Principal principal, MatchingPostUpdateRequestDto matchingPostUpdateRequestDto) {
        MatchingPost matchingPost = matchingPostRepository.findById(matchingPostId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 게시물입니다"));

        Long userId = Long.valueOf(principal.getName());
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 사용자입니다"));

        if (user.getNickname().equals(matchingPost.getUser().getNickname())) {
            return MatchingPostUpdateResponseDto.of(
                    matchingPost.getMatchingPostId(),
                    user.getNickname(),
                    String.valueOf(matchingPost.getCategory()),
                    String.valueOf(matchingPost.getStatus()),
                    matchingPost.getTitle(),
                    matchingPost.getContent(),
                    matchingPost.getViewCount(),
                    matchingPost.getUpdateDate()
            );
        }
        else throw new AuthorizationServiceException("잘못된 접근입니다");
    }
}


