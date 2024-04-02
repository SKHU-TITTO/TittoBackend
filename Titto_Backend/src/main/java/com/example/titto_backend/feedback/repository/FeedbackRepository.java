package com.example.titto_backend.feedback.repository;

import com.example.titto_backend.feedback.domain.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
}
