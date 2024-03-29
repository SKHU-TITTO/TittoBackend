package com.example.titto_backend.questionBoard.repository;

import com.example.titto_backend.auth.domain.User;
import com.example.titto_backend.questionBoard.domain.Department;
import com.example.titto_backend.questionBoard.domain.Question;
import com.example.titto_backend.questionBoard.domain.Status;
import com.example.titto_backend.questionBoard.dto.QuestionInfoDTO;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    Page<Question> findByDepartmentOrderByCreateDateDesc(Pageable pageable, Department category);

    Page<Question> findAllByOrderByCreateDateDesc(Pageable pageable);

    Page<Question> findQuestionByStatus(Status status, Pageable pageable);

    Page<Question> findByTitleContaining(String keyWord, Pageable pageable);

    @Query("SELECT new com.example.titto_backend.questionBoard.dto.QuestionInfoDTO(a.id, a.title, a.content,"
            + " a.createDate, a.viewCount, a.answerCount, a.department) "
            + "FROM Question a"
            + " WHERE a.author = :user")
    List<QuestionInfoDTO> findQuestionsInfoByAuthor(@Param("user") User user);

    List<Question> findQuestionsByAuthor(User user);
}
