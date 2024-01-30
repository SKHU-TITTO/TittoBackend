package com.example.titto_backend.matchingBoard.repository.matchingBoard;

import com.example.titto_backend.matchingBoard.domain.matchingBoard.MatchingPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchingPostRepository extends JpaRepository<MatchingPost, Long> {
}
