package com.example.titto_backend.repository;

import com.example.titto_backend.domain.review.MatchingPostReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<MatchingPostReview, Integer> {

}
