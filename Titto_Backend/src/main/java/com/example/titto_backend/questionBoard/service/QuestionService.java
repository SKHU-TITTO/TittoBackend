package com.example.titto_backend.questionBoard.service;

import com.example.titto_backend.auth.domain.User;
import com.example.titto_backend.auth.repository.UserRepository;
import com.example.titto_backend.auth.service.BadgeService;
import com.example.titto_backend.auth.service.ExperienceService;
import com.example.titto_backend.common.exception.CustomException;
import com.example.titto_backend.common.exception.ErrorCode;
import com.example.titto_backend.common.util.RedisUtil;
import com.example.titto_backend.questionBoard.domain.Answer;
import com.example.titto_backend.questionBoard.domain.Department;
import com.example.titto_backend.questionBoard.domain.Question;
import com.example.titto_backend.questionBoard.domain.Status;
import com.example.titto_backend.questionBoard.dto.QuestionDTO;
import com.example.titto_backend.questionBoard.dto.QuestionDTO.Response;
import com.example.titto_backend.questionBoard.repository.AnswerRepository;
import com.example.titto_backend.questionBoard.repository.QuestionRepository;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
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
    private final AnswerRepository answerRepository;

    private final ExperienceService experienceService;
    private final AnswerService answerService;
    private final RedisUtil redisUtil;
    private final BadgeService badgeService;

    @Transactional
    public String save(String email, QuestionDTO.Request request) throws CustomException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        experienceService.deductExperience(user, request.getSendExperience());
        user.setCountQuestion(user.getCountQuestion() + 1);
        badgeService.getQuestionBadge(user, user.getCountQuestion());

        questionRepository.save(Question.builder()
                .title(request.getTitle())
                .author(user)
                .content(request.getContent())
                .department(Department.valueOf(request.getDepartment().toUpperCase()))
                .status(Status.UNSOLVED)
                .sendExperience(request.getSendExperience())
                .viewCount(0)
                .answerCount(0)
                .isAnswerAccepted(false)
                .build());

        return "질문이 성공적으로 등록되었습니다.";
    }

    @Transactional(readOnly = true)
    public Page<QuestionDTO.Response> findAll(int page) {
        Pageable pageable = Pageable.ofSize(10).withPage(page);
        return questionRepository.findAllByOrderByCreateDateDesc(pageable).map(QuestionDTO.Response::new);
    }

    @Transactional(readOnly = true)
    public Page<QuestionDTO.Response> findByStatus(int page, String status) {
        Pageable pageable = Pageable.ofSize(10).withPage(page);
        return questionRepository.findQuestionByStatus(Status.valueOf(status), pageable).map(QuestionDTO.Response::new);
    }

    @Transactional
    public QuestionDTO.Response findById(Principal principal, Long Id) {
        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        Question question = questionRepository.findById(Id)
                .orElseThrow(() -> new CustomException(ErrorCode.QUESTION_NOT_FOUND));
        countViews(user, question);
        return new Response(question);
    }

    @Transactional(readOnly = true)
    public Page<QuestionDTO.Response> findByCategory(int page, String category) {
        Pageable pageable = Pageable.ofSize(10).withPage(page);
        return questionRepository.findByDepartmentOrderByCreateDateDesc(pageable,
                        Department.valueOf(category.toUpperCase()))
                .map(QuestionDTO.Response::new);
    }

    @Transactional(readOnly = true)
    public Page<QuestionDTO.Response> searchByKeyword(String keyWord, int page) {
        Pageable pageable = Pageable.ofSize(10).withPage(page);
        return questionRepository.findByTitleContaining(keyWord, pageable).map(QuestionDTO.Response::new);
    }

    @Transactional
    public void update(QuestionDTO.Update update, Long id, User user) throws CustomException {
        validateAuthorIsLoggedInUser(id, user);
        Question oldQuestion = questionRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.QUESTION_NOT_FOUND));

        experienceService.deductExperience(user, update.getSendExperience()); // 유저 경험치 차감

        oldQuestion.update(
                update.getTitle(),
                update.getContent(),
                Department.valueOf(String.valueOf(update.getDepartment())),
                update.getSendExperience()
        );
    }

    @Transactional
    public void delete(Long id, User user) {
        validateAuthorIsLoggedInUser(id, user);
        user.setCountQuestion(user.getCountQuestion() - 1);

        List<Answer> answers = answerRepository.findByQuestionId(id);

        answers.forEach(answer -> {
            User answerAuthor = answer.getAuthor();
            answerService.delete(answer.getId(), answerAuthor);
        });

        questionRepository.deleteById(id);
    }

    private void isAcceptAnswer(Question question, User user) {
        if (!question.isAnswerAccepted()) {
            user.setCurrentExperience(user.getCurrentExperience() + question.getSendExperience());
        } else {
            throw new CustomException(ErrorCode.DELETE_NOT_ALLOWED);
        }
    }

    private void validateAuthorIsLoggedInUser(Long id, User user) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.QUESTION_NOT_FOUND));

        isAcceptAnswer(question, user);

        if (!question.getAuthor().equals(user)) {
            throw new CustomException(ErrorCode.MISMATCH_AUTHOR);
        }
    }

    @Transactional
    public void countViews(User user, Question question) {
        String key = String.format("QuestionBoardViewCount:%d:%d", user.getId(), question.getId());
        String viewCount = redisUtil.getData(key);

        if (viewCount == null) {
            redisUtil.setDateExpire(key, "1", calculateTimeUntilMidnight());
            question.addViewCount();
        } else {
            int count = Integer.parseInt(viewCount);
            if (count < 1) {
                count++;
                redisUtil.setDateExpire(key, String.valueOf(count), calculateTimeUntilMidnight());
                question.addViewCount();
            }
        }
    }

    public static long calculateTimeUntilMidnight() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime midnight = now.truncatedTo(ChronoUnit.DAYS).plusDays(1);
        return ChronoUnit.SECONDS.between(now, midnight);
    }

}