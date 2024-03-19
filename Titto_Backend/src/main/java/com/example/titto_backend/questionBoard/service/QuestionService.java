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

    //Create
    @Transactional
    public String save(String email, QuestionDTO.Request request) throws CustomException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        experienceService.deductExperience(user, request.getSendExperience()); // 유저 경험치 차감
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

    // Update
    @Transactional
    public void update(QuestionDTO.Update update, Long id, User user) throws CustomException {
        validateAuthorIsLoggedInUser(id, user);
        Question oldQuestion = questionRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.QUESTION_NOT_FOUND));

        oldQuestion.update(
                update.getTitle(),
                update.getContent(),
                Department.valueOf(String.valueOf(update.getDepartment()))
        );
    }

    // Delete
    @Transactional
    public void delete(Long id, User user) {
        validateAuthorIsLoggedInUser(id, user);
        user.setCountQuestion(user.getCountQuestion() - 1);
        // 질문에 연관된 답변들을 가져옴
        List<Answer> answers = answerRepository.findByQuestionId(id);

        // 답변들을 하나씩 삭제
        answers.forEach(answer -> {
            User answerAuthor = answer.getAuthor();
            answerService.delete(answer.getId(), answerAuthor);
        });

        // 질문 삭제
        questionRepository.deleteById(id);
    }

    // 질문 게시판에 채택된 답변이 없으면 걸었던 경험치 되돌려줌, 채택된 답변 있을 시 삭제 불가능
    private void isAcceptAnswer(Question question, User user) {
        if (!question.isAnswerAccepted()) {
            user.setCurrentExperience(user.getCurrentExperience() + question.getSendExperience());
        } else {
            throw new CustomException(ErrorCode.DELETE_NOT_ALLOWED);
        }
    }

    // 글을 쓴 사람과 현재 로그인한 사람이 같은지 확인
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