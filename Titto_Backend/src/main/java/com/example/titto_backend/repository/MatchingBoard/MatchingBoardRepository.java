package com.example.titto_backend.repository.MatchingBoard;

import com.example.titto_backend.domain.matchingBoard.MatchingBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchingBoardRepository extends JpaRepository<MatchingBoard, Long> {
    Page<MatchingBoard> findAll(Pageable pageable);
}
