package com.example.titto_backend.repository;

import com.example.titto_backend.domain.matchingBoard.MatchingPost;
import com.example.titto_backend.domain.review.MatchingPostReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchingPostReviewRepository extends JpaRepository<MatchingPostReview, Long> {
    List<MatchingPostReview> findAllByMatchingPost(MatchingPost matchingPost);
}
