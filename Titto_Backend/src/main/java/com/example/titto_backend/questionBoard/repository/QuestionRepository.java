package com.example.titto_backend.questionBoard.repository;

import com.example.titto_backend.auth.domain.User;
import com.example.titto_backend.questionBoard.domain.Department;
import com.example.titto_backend.questionBoard.domain.Question;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    Page<Question> findByDepartmentOrderByCreateDateDesc(Pageable pageable, Department category);

    Page<Question> findAllByOrderByCreateDateDesc(Pageable pageable);

    boolean existsByIdAndAcceptedAnswerIsNotNull(Long id); // 채택된 답변이 있는지 확인

    List<Question> findQuestionByAuthor(User user);


}
