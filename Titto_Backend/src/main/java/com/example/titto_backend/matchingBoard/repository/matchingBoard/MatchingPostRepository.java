package com.example.titto_backend.matchingBoard.repository.matchingBoard;

import com.example.titto_backend.auth.domain.User;
import com.example.titto_backend.matchingBoard.domain.matchingBoard.Category;
import com.example.titto_backend.matchingBoard.domain.matchingBoard.MatchingPost;
import com.example.titto_backend.matchingBoard.dto.response.matchingPostResponse.MatchingPostInfoDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchingPostRepository extends JpaRepository<MatchingPost, Long> {
    Page<MatchingPost> findByTitleContaining(String keyword, Pageable pageable);

    Page<MatchingPost> findByCategory(Category category, Pageable pageable);

    @Query("SELECT new com.example.titto_backend.matchingBoard.dto.response.matchingPostResponse.MatchingPostInfoDto(a.matchingPostId, a.title, a.content, "
            + "a.createDate, a.viewCount, a.reviewCount, a.category) "
            + "FROM MatchingPost a"
            + " WHERE a.user = :user")
    List<MatchingPostInfoDto> findMatchingPostsInfoByAuthor(@Param("user") User user);

}
