package com.example.titto_backend.questionBoard.repository;

import com.example.titto_backend.auth.domain.User;
import com.example.titto_backend.questionBoard.domain.Answer;
import com.example.titto_backend.questionBoard.dto.AnswerInfoDTO;
import io.lettuce.core.dynamic.annotation.Param;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

    @Query("SELECT new com.example.titto_backend.questionBoard.dto.AnswerInfoDTO(a.id, a.content, a.question.id, a.question.title, a.question.department) "
            + "FROM Answer a"
            + " WHERE a.author = :user")
    List<AnswerInfoDTO> findAnswersInfoByAuthor(@Param("user") User user);

    List<Answer> findByQuestionId(Long questionId);

    List<Answer> findAnswersByAuthor(User user);
}
