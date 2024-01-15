package com.example.titto_backend.repository.MatchingBoard;

import com.example.titto_backend.domain.MatchingBoard.MatchingPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchingPostRepository extends JpaRepository<MatchingPost, Long> {

}
