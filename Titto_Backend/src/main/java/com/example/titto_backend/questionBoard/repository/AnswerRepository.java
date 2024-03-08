package com.example.titto_backend.questionBoard.repository;

import com.example.titto_backend.auth.domain.User;
import com.example.titto_backend.questionBoard.domain.Answer;
import com.example.titto_backend.questionBoard.dto.AnswerInfoDto;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface AnswerRepository extends CrudRepository<Answer, Long> {

    @Query("SELECT new com.example.titto_backend.questionBoard.dto.AnswerInfoDto(a.id, a.content) FROM Answer a WHERE a.author = :user")
    List<AnswerInfoDto> findAnswerInfoByAuthor(@Param("user") User user);

    List<Answer> findByQuestionId(Long questionId);
}
