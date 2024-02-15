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
    return questionRepository.findByDepartmentOrderByCreateDateDesc(pageable, Department.valueOf(category.toUpperCase()))
            .map(QuestionDTO.Response::new);
  }

  // Update
  // 작성자만 수정이 가능하도록!
  @Transactional //더티 체킹 후 알아서 update 쿼리 생성해서 db로 commit을 날려줌!! -> 변경사항 적용
  public void update(String email, QuestionDTO.Update update, Long id) throws CustomException {
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
  // 작성자만 삭제가 가능하도록!
  @Transactional
  public void delete(Long id) {
    questionRepository.deleteById(id);
  }
}
