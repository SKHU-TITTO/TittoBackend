package com.example.titto_backend.questionBoard.service;

import com.example.titto_backend.auth.domain.User;
import com.example.titto_backend.auth.repository.UserRepository;
import com.example.titto_backend.auth.service.BadgeService;
import com.example.titto_backend.auth.service.ExperienceService;
import com.example.titto_backend.common.exception.CustomException;
import com.example.titto_backend.common.exception.ErrorCode;
import com.example.titto_backend.questionBoard.domain.Answer;
import com.example.titto_backend.questionBoard.domain.Question;
import com.example.titto_backend.questionBoard.domain.Status;
import com.example.titto_backend.questionBoard.dto.AnswerDTO;
import com.example.titto_backend.questionBoard.dto.AnswerDTO.Response;
import com.example.titto_backend.questionBoard.repository.AnswerRepository;
import com.example.titto_backend.questionBoard.repository.QuestionRepository;
import java.util.List;
import java.util.stream.Collectors;
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
    private final BadgeService badgeService;

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
        question.setAnswerCount(question.getAnswerCount() + 1);
        badgeService.getAnswerBadge(user, user.getCountAnswer());  // 뱃지 여부 판단

        // 답변을 작성한 사용자의 경험치 추가
        experienceService.addExperience(question.getAuthor(), user, 5);

        return new AnswerDTO.Response(savedAnswer);
    }

    @Transactional
    public List<Response> findAnswersByPostId(Long postId) {
        Question question = questionRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.QUESTION_NOT_FOUND));
        List<Answer> answers = question.getAnswers();
        return answers.stream()
                .map(AnswerDTO.Response::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public AnswerDTO.Response update(Long id, AnswerDTO.Request request, User user) throws CustomException {
        Answer answer = answerRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.ANSWER_NOT_FOUND));
        validateAnswerAuthorIsLoggedInUser(answer, user);
        answer.setContent(request.getContent());
        return new AnswerDTO.Response(answer);
    }

    @Transactional
    public void delete(Long answerId, User user) throws CustomException {
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new CustomException(ErrorCode.ANSWER_NOT_FOUND));
        validateAnswerAuthorIsLoggedInUser(answer, user);
        user.setCountAnswer(user.getCountAnswer() - 1);  // 유저 답변 수 1 감소

        Question question = answer.getQuestion();
        question.setAnswerCount(question.getAnswerCount() - 1);

        User answerAuthor = answer.getAuthor();
        answerAuthor.setTotalExperience(answerAuthor.getTotalExperience() - 5);
        answerAuthor.setCurrentExperience(answerAuthor.getCurrentExperience() - 5);

        answerRepository.deleteById(answerId);
    }

    @Transactional
    public void acceptAnswer(Long questionId, Long answerId, User user) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new CustomException(ErrorCode.QUESTION_NOT_FOUND));
        validateQuestionAuthorIsLoggedInUser(question, user);

        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new CustomException(ErrorCode.ANSWER_NOT_FOUND));

        if (question.isAnswerAccepted()) {
            throw new CustomException(ErrorCode.CANNOT_ACCEPTED);
        }

        answer.setAccepted(true);
        question.setStatus(Status.valueOf("SOLVED"));
        question.setAnswerAccepted(true);  // 일단 임시 추가

        User answerAuthor = answer.getAuthor();
        Integer updateCountAccept = answerAuthor.getCountAccept() + 1;
        answerAuthor.setCountAccept(updateCountAccept);

        experienceService.addExperience(question.getAuthor(), answerAuthor, 35 + question.getSendExperience());
        badgeService.getAcceptBadge(answerAuthor, answerAuthor.getCountAccept());
    }

    private void validateQuestionAuthorIsLoggedInUser(Question question, User user) {
        if (!question.getAuthor().equals(user)) {
            throw new CustomException(ErrorCode.MISMATCH_AUTHOR);
        }
    }

    private void validateAnswerAuthorIsLoggedInUser(Answer answer, User user) {
        if (!answer.getAuthor().equals(user)) {
            throw new CustomException(ErrorCode.MISMATCH_AUTHOR);
        }
    }

}
