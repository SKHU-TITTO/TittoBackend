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
    return questionRepository.findAllByOrderByCreateDateDesc(pageable).map(QuestionDTO.Response::new);
  }

  @Transactional(readOnly = true)
  public Response findById(Long postId) {
    Question question = questionRepository.findById(postId)
            .orElseThrow(() -> new CustomException(ErrorCode.QUESTION_NOT_FOUND));
    return new Response(question);
  }

  @Transactional(readOnly = true)
  public Page<QuestionDTO.Response> findByCategory(Pageable pageable, String category) {
    return questionRepository.findByDepartmentOrderByCreateDateDesc(pageable,
                    Department.valueOf(category.toUpperCase()))
            .map(QuestionDTO.Response::new);
  }

  // Update
  @Transactional
  public void update(QuestionDTO.Update update, Long id, Long userId) throws CustomException {

    // 현재 사용자가 작성자인지 확인
    if (!isAuthor(id, userId)) {
      throw new CustomException(ErrorCode.MISMATCH_AUTHOR);
    }

    // 작성자일 경우에만 수정 가능하도록 처리
    Question oldQuestion = questionRepository.findById(id)
            .orElseThrow(() -> new CustomException(ErrorCode.QUESTION_NOT_FOUND));
    oldQuestion.update(
            update.getTitle(),
            update.getContent(),
            Department.valueOf(String.valueOf(update.getDepartment())),
            Status.valueOf(String.valueOf(update.getStatus()))
    );
  }

  // Delete
  @Transactional
  public void delete(Long id, Long userId) {

    if (!isAuthor(id, userId)) {
      throw new CustomException(ErrorCode.MISMATCH_AUTHOR);
    }

    questionRepository.deleteById(id);
  }

  public boolean isAuthor(Long id, Long userId) {
    User user = userRepository.findById(userId)
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    Question question = questionRepository.findById(id)
            .orElseThrow(() -> new CustomException(ErrorCode.QUESTION_NOT_FOUND));
    return question.getAuthor().getId().equals(user.getId());
  }

}
