package com.example.titto_backend.questionBoard.repository;

import com.example.titto_backend.auth.domain.User;
import com.example.titto_backend.questionBoard.domain.Answer;
import com.example.titto_backend.questionBoard.domain.Question;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    void deleteAllByQuestion(Question question);

    List<Answer> findAnswerByAuthor(User user);
}
