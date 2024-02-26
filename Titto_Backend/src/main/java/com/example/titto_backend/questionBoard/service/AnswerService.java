package com.example.titto_backend.questionBoard.service;

import com.example.titto_backend.auth.domain.User;
import com.example.titto_backend.auth.repository.UserRepository;
import com.example.titto_backend.auth.service.ExperienceService;
import com.example.titto_backend.common.exception.CustomException;
import com.example.titto_backend.common.exception.ErrorCode;
import com.example.titto_backend.questionBoard.domain.Answer;
import com.example.titto_backend.questionBoard.domain.Question;
import com.example.titto_backend.questionBoard.dto.AnswerDTO;
import com.example.titto_backend.questionBoard.repository.AnswerRepository;
import com.example.titto_backend.questionBoard.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final ExperienceService experienceService;

    // Create
    @Transactional
    public AnswerDTO.Response save(AnswerDTO.Request request, Long questionId, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new CustomException(ErrorCode.QUESTION_NOT_FOUND));

        Answer answer = Answer.builder()
                .question(question)
                .author(user)
                .content(request.getContent())
                .build();

        Answer savedAnswer = answerRepository.save(answer);
        Integer updateUserCountAnswer = user.getCountAnswer() + 1;
        user.setCountAnswer(updateUserCountAnswer);

        // 답변을 작성한 사용자의 경험치 추가
        experienceService.addExperience(question.getAuthor(), user, 20);

        return new AnswerDTO.Response(savedAnswer);
    }

    //Update
    @Transactional
    public AnswerDTO.Response update(Long id, AnswerDTO.Request request, Long userId) throws CustomException {
        validateAuthorIsLoggedInUser(id, userId);
        Answer answer = answerRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.ANSWER_NOT_FOUND));
        answer.setContent(request.getContent());
        return new AnswerDTO.Response(answer);
    }

    // Delete
    @Transactional
    public void delete(Long id, Long userId) throws CustomException {
        validateAuthorIsLoggedInUser(id, userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        Integer updateCountAnswer = user.getCountAnswer() - 1;
        user.setCountAnswer(updateCountAnswer);
        answerRepository.deleteById(id);
    }

    @Transactional
    public void acceptAnswer(Long questionId, Long answerId, Long userId) {
        validateAuthorIsLoggedInUser(answerId, userId);
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new CustomException(ErrorCode.QUESTION_NOT_FOUND));
        verifyAcceptedAnswer(questionId);
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new CustomException(ErrorCode.ANSWER_NOT_FOUND));
        answer.setAccepted(true);
        question.setAcceptedAnswer(answer);

        question.setAnswerAccepted(true);  // 일단 임시 추가

        Integer updateCountAccept = answer.getAuthor().getCountAccept() + 1;
        answer.getAuthor().setCountAccept(updateCountAccept);

        experienceService.addExperience(question.getAuthor(), answer.getAuthor(), 35 + question.getSendExperience());
    }

    private void verifyAcceptedAnswer(Long questionId) {
        if (questionRepository.existsByIdAndAcceptedAnswerIsNotNull(questionId)) {
            throw new CustomException(ErrorCode.ALREADY_ACCEPTED_ANSWER);
        }
    }

    private void validateAuthorIsLoggedInUser(Long id, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.QUESTION_NOT_FOUND));
        if (!question.getAuthor().getId().equals(user.getId())) {
            throw new CustomException(ErrorCode.MISMATCH_AUTHOR);
        }
    }

}
