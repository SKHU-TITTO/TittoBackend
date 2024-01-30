package com.example.titto_backend.matchingBoard.repository.matchingBoard;

import com.example.titto_backend.matchingBoard.domain.matchingBoard.MatchingBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchingBoardRepository extends JpaRepository<MatchingBoard, Long> {
    Page<MatchingBoard> findAll(Pageable pageable);
}
