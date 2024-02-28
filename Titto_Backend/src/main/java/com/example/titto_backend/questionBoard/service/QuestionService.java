package com.example.titto_backend.questionBoard.service;

import com.example.titto_backend.auth.domain.User;
import com.example.titto_backend.auth.repository.UserRepository;
import com.example.titto_backend.auth.service.ExperienceService;
import com.example.titto_backend.common.exception.CustomException;
import com.example.titto_backend.common.exception.ErrorCode;
import com.example.titto_backend.questionBoard.domain.Answer;
import com.example.titto_backend.questionBoard.domain.Department;
import com.example.titto_backend.questionBoard.domain.Question;
import com.example.titto_backend.questionBoard.domain.Status;
import com.example.titto_backend.questionBoard.dto.QuestionDTO;
import com.example.titto_backend.questionBoard.dto.QuestionDTO.Response;
import com.example.titto_backend.questionBoard.repository.AnswerRepository;
import com.example.titto_backend.questionBoard.repository.QuestionRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.Arrays;
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

    //Create
    @Transactional
    public String save(String email, QuestionDTO.Request request) throws CustomException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        experienceService.deductExperience(user, request.getSendExperience()); // 유저 경험치 차감

        questionRepository.save(Question.builder()
                .title(request.getTitle())
                .author(user)
                .content(request.getContent())
                .department(Department.valueOf(request.getDepartment().toUpperCase()))
                .status(Status.valueOf("UNSOLVED"))
                .sendExperience(request.getSendExperience())
                .viewCount(0)
                .isAnswerAccepted(false)
                .build());
        return "질문이 성공적으로 등록되었습니다.";
    }

    /*// Keyword가 null이면 전체목록, 있으면 검색
    @Transactional(readOnly = true)
    public Page<Response> findAll(int page, String keyword) {
        Pageable pageable = Pageable.ofSize(10).withPage(page);
        Specification<Question> spec = searchByKeyword(keyword);
        if (keyword == null) {
            return questionRepository.findAllByOrderByCreateDateDesc(pageable).map(QuestionDTO.Response::new);
        }
        return questionRepository.findAll(spec, pageable).map(QuestionDTO.Response::new);
    }*/

    @Transactional(readOnly = true)
    public Page<Response> findByStatus(int page, String status) {
        Pageable pageable = Pageable.ofSize(10).withPage(page);
        return questionRepository.findQuestionByStatus(Status.valueOf(status), pageable).map(QuestionDTO.Response::new);
    }

    @Transactional
    public Response findById(Long Id, HttpServletRequest request, HttpServletResponse response) {
        Question question = questionRepository.findById(Id)
                .orElseThrow(() -> new CustomException(ErrorCode.QUESTION_NOT_FOUND));
        validViewCount(question, request, response);
        return new Response(question);
    }

    @Transactional(readOnly = true)
    public Page<QuestionDTO.Response> findByCategory(int page, String category) {
        Pageable pageable = Pageable.ofSize(10).withPage(page);
        return questionRepository.findByDepartmentOrderByCreateDateDesc(pageable,
                        Department.valueOf(category.toUpperCase()))
                .map(QuestionDTO.Response::new);
    }

    /*// Keyword Search
    private Specification<Question> searchByKeyword(String keyword) {
        return new Specification<>() {
            @Serial
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<Question> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true);
                Join<Question, User> u1 = q.join("author", JoinType.LEFT);
                Join<Question, Answer> a = q.join("answers", JoinType.LEFT);
                Join<Answer, User> u2 = a.join("author", JoinType.LEFT);
                return cb.or(
                        cb.like(q.get("title"), "%" + keyword + "%"),
                        cb.like(q.get("content"), "%" + keyword + "%"),
                        cb.like(u1.get("nickname"), "%" + keyword + "%"),
                        cb.like(u2.get("nickname"), "%" + keyword + "%")
                );
            }
        };*/
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

    // 질문 게시판에 채택된 답변이 없으면 경험치 되돌려줌, 채택된 답변 있을 시 삭제 불가능
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

    // 조회수 증가
    private void validViewCount(Question question, HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        System.out.println("cookies = " + Arrays.toString(cookies));
        Cookie cookie = null;
        boolean isCookie = false;
        for (Cookie value : cookies) {
            if (value.getName().equals("viewCookie")) {
                cookie = value;
                if (!cookie.getValue().contains("[" + question.getId() + "]")) {
                    question.addViewCount();
                    cookie.setValue(cookie.getValue() + "[" + question.getId() + "]");
                }
                isCookie = true;
                break;
            }
        }
        System.out.println("isCookie = " + isCookie);
        if (!isCookie) {
            question.addViewCount();
            cookie = new Cookie("viewCookie", "[" + question.getId() + "]");
        }
        long todayEndSecond = LocalDate.now().atTime(LocalTime.MAX).toEpochSecond(ZoneOffset.UTC);
        long currentSecond = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        cookie.setPath("/");
        cookie.setMaxAge((int) (todayEndSecond - currentSecond));
        response.addCookie(cookie);
    }

}
