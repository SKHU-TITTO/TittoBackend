package com.example.titto_backend.matchingBoard.repository.review;

import com.example.titto_backend.matchingBoard.domain.matchingBoard.MatchingPost;
import com.example.titto_backend.matchingBoard.domain.review.MatchingPostReview;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchingPostReviewRepository extends JpaRepository<MatchingPostReview, Long> {
    List<MatchingPostReview> findAllByMatchingPost(MatchingPost matchingPost);

    void deleteAllByMatchingPost(MatchingPost matchingPost);

    Integer countByMatchingPost(MatchingPost matchingPost);
}
