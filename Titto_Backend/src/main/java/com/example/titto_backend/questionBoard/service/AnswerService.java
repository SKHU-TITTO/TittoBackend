package com.example.titto_backend.questionBoard.service;

import com.example.titto_backend.auth.domain.User;
import com.example.titto_backend.auth.repository.UserRepository;
import com.example.titto_backend.common.exception.CustomException;
import com.example.titto_backend.common.exception.ErrorCode;
import com.example.titto_backend.questionBoard.domain.Answer;
import com.example.titto_backend.questionBoard.domain.Question;
import com.example.titto_backend.questionBoard.dto.AnswerDTO;
import com.example.titto_backend.questionBoard.dto.AnswerDTO.Response;
import com.example.titto_backend.questionBoard.repository.AnswerRepository;
import com.example.titto_backend.questionBoard.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AnswerService {

  private final AnswerRepository answerRepository;
  private final QuestionRepository questionRepository;
  private final UserRepository userRepository;

  // Create
  @Transactional
  public AnswerDTO.Response save(AnswerDTO.Request request, Long postId, String email) throws CustomException {
    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    Answer answer = Answer.builder()
            .question(questionRepository.findById(postId)
                    .orElseThrow(() -> new CustomException(ErrorCode.QUESTION_NOT_FOUND)))
            .author(user)
            .content(request.getContent())
            .isAccepted(false)
            .build();
    return new AnswerDTO.Response(answerRepository.save(answer));
  }

  @Transactional(readOnly = true)
  public Page<Response> findAll(Pageable pageable) {
    return answerRepository.findAllByOrderByCreateDateDesc(pageable).map(AnswerDTO.Response::new);
  }

  @Transactional(readOnly = true)
  public AnswerDTO.Response findById(Long postId) {
    Answer answer = answerRepository.findById(postId)
            .orElseThrow(() -> new CustomException(ErrorCode.ANSWER_NOT_FOUND));
    return new Response(answer);
  }

  // Delete
  @Transactional
  public void delete(Long id, Long userId) throws CustomException {
    if (!isAuthor(id, userId)) {
      throw new CustomException(ErrorCode.MISMATCH_AUTHOR);
    }
    answerRepository.deleteById(id);
  }

  public boolean isAuthor(Long id, Long userId) {
    User user = userRepository.findById(userId)
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    Answer answer = answerRepository.findById(id)
            .orElseThrow(() -> new CustomException(ErrorCode.QUESTION_NOT_FOUND));
    return answer.getAuthor().getId().equals(user.getId());
  }

}
