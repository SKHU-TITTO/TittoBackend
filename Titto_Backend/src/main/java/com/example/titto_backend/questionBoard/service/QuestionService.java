package com.example.titto_backend.questionBoard.service;

import com.example.titto_backend.auth.domain.User;
import com.example.titto_backend.auth.repository.UserRepository;
import com.example.titto_backend.common.exception.CustomException;
import com.example.titto_backend.common.exception.ErrorCode;
import com.example.titto_backend.questionBoard.domain.Department;
import com.example.titto_backend.questionBoard.domain.Question;
import com.example.titto_backend.questionBoard.domain.Status;
import com.example.titto_backend.questionBoard.dto.QuestionDTO;
import com.example.titto_backend.questionBoard.dto.QuestionDTO.Response;
import com.example.titto_backend.questionBoard.repository.QuestionRepository;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class QuestionService {
  private final QuestionRepository questionRepository;
  private final UserRepository userRepository;

  //Create
  @Transactional
  public QuestionDTO.Response save(String email, QuestionDTO.Request request) throws CustomException {
    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    System.out.println("여긴 지나감");

    System.out.println(request.getDepartment());
    System.out.println(request.getStatus());
    System.out.println(request.getTitle());
    System.out.println(request.getContent());
    for (String image : request.getImageList()) {
      System.out.println(image);
    }

    Question question = Question.builder()
            .title(request.getTitle())
            .author(user)
            .content(request.getContent())
            .department(Department.valueOf(request.getDepartment().toUpperCase()))
            .status(Status.valueOf(request.getStatus().toUpperCase()))
            .build();
    return new Response(questionRepository.save(question));
  }

  @Transactional(readOnly = true)
  public Page<Response> findAll(Pageable pageable) {
    return questionRepository.findAll(pageable).map(QuestionDTO.Response::new);
  }

  @Transactional(readOnly = true)
  public Response findById(Long postId) {
    Question question = questionRepository.findById(postId)
            .orElseThrow(() -> new CustomException(ErrorCode.QUESTION_NOT_FOUND));
    return new Response(question);
  }

  @Transactional(readOnly = true)
  public Page<QuestionDTO.Response> findByCategory(Pageable pageable, String category) {
    return questionRepository.findByDepartment(pageable, Department.valueOf(category.toUpperCase()))
            .map(QuestionDTO.Response::new);
  }

  //Update
  @Transactional
  public QuestionDTO.Response update(String email, QuestionDTO.Update update, Long id) throws CustomException {
    Question oldQuestion = questionRepository.findById(id)
            .orElseThrow(() -> new CustomException(ErrorCode.QUESTION_NOT_FOUND));
    Question newQuestion;
    if (oldQuestion.getAuthor().getEmail().equals(email)) {
      newQuestion = Question.builder()
              .id(id)
              .title(update.getTitle())
              .content(update.getContent())
              .department(Department.valueOf(String.valueOf(update.getDepartment())))
              .status(Status.valueOf(String.valueOf(update.getStatus())))
              .build();
    } else {
      throw new CustomException(ErrorCode.USER_NOT_MATCH);
    }
    return new Response(newQuestion);
  }

  //Delete
}
