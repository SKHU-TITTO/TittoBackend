package com.example.titto_backend.question;

import com.example.titto_backend.questionBoard.domain.Department;
import com.example.titto_backend.questionBoard.domain.Question;
import com.example.titto_backend.questionBoard.repository.QuestionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class QuestionTest {
  @Autowired
  private QuestionRepository questionRepository;

  @Test
  void JpaTest() {
    Question q1 = new Question();
    q1.setTitle("title1");
    q1.setContent("content1");
    q1.setDepartment(Department.SOFEWARE);

    questionRepository.save(q1);

    Question q2 = new Question();
    q2.setTitle("title2");
    q2.setContent("content2");
    q2.setDepartment(Department.FUTURE_FUSION);

    questionRepository.save(q2);

  }
}
