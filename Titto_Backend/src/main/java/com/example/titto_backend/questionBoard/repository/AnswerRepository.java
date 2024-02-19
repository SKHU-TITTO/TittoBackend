package com.example.titto_backend.questionBoard.repository;

import com.example.titto_backend.questionBoard.domain.Answer;
import com.example.titto_backend.questionBoard.domain.Question;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
  Page<Answer> findAllByOrderByCreateDateDesc(Pageable pageable);

  boolean existsByQuestionIdAndIsAcceptedTrue(Long questionId);
}
