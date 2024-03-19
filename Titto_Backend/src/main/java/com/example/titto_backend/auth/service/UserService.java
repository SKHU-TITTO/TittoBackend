package com.example.titto_backend.auth.service;

import com.example.titto_backend.auth.domain.User;
import com.example.titto_backend.auth.dto.request.SignUpDTO;
import com.example.titto_backend.auth.dto.request.UserInfoUpdateDTO;
import com.example.titto_backend.auth.dto.request.UserProfileUpdateDTO;
import com.example.titto_backend.auth.dto.response.UserInfoDTO;
import com.example.titto_backend.auth.dto.response.UserProfileViewDto;
import com.example.titto_backend.auth.dto.response.UserRankingDto;
import com.example.titto_backend.auth.repository.UserRepository;
import com.example.titto_backend.common.exception.CustomException;
import com.example.titto_backend.common.exception.ErrorCode;
import com.example.titto_backend.matchingBoard.domain.matchingBoard.MatchingPost;
import com.example.titto_backend.matchingBoard.domain.review.MatchingPostReview;
import com.example.titto_backend.matchingBoard.repository.matchingBoard.MatchingPostRepository;
import com.example.titto_backend.matchingBoard.repository.review.MatchingPostReviewRepository;
import com.example.titto_backend.questionBoard.domain.Answer;
import com.example.titto_backend.questionBoard.domain.Question;
import com.example.titto_backend.questionBoard.dto.AnswerInfoDTO;
import com.example.titto_backend.questionBoard.repository.AnswerRepository;
import com.example.titto_backend.questionBoard.repository.QuestionRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final MatchingPostRepository matchingPostRepository;
    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final MatchingPostReviewRepository matchingPostReviewRepository;

    // 닉네임, 학번 중복 여부 확인
    @Transactional
    public void signUp(SignUpDTO signUpDTO, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        user.signupUser(signUpDTO);
    }

    //유저 정보 불러오기
    @Transactional(readOnly = true)
    public UserInfoDTO getUser(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        return userOptional.map(UserInfoDTO::new).orElse(null);
    }

    @Transactional
    public void updateNickname(String email, UserInfoUpdateDTO requestDTO) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (requestDTO.getNewNickname() != null && isDuplicatedNickname(requestDTO.getNewNickname())) {
            throw new CustomException(ErrorCode.DUPLICATED_NICKNAME);
        }

        if (requestDTO.getNewNickname() != null) {
            user.setNickname(requestDTO.getNewNickname());
        }
    }

    //유저 프로필(한줄소개, 자기소개) 수정
    @Transactional
    public void updateUserProfile(String email, UserProfileUpdateDTO userProfileUpdateDTO) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        user.setOneLineIntro(userProfileUpdateDTO.getOneLineIntro());
        user.setSelfIntro(userProfileUpdateDTO.getSelfIntro());
    }

    // 유저 프로필 조회
    public UserProfileViewDto userProfileView(Long userId) {
        if (userId == 1L) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        return UserProfileViewDto.of(user);
    }

    // 유저 작성 글 보기
    public List<Object> userPostsView(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        List<Object> questionPosts = new ArrayList<>(questionRepository.findQuestionsInfoByAuthor(user));
        List<Object> matchingPosts = new ArrayList<>(matchingPostRepository.findMatchingPostsInfoByAuthor(user));

        return Stream.concat(questionPosts.stream(), matchingPosts.stream())
                .toList();
    }

    // 유저 작성 답글 보기
    public List<AnswerInfoDTO> userAnswerView(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return answerRepository.findAnswersInfoByAuthor(user);
    }

    @Transactional
    public void deleteUser(Long userId) {
        // 삭제할 유저 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // dummyUser 조회 (예: id가 0인 사용자)
        User dummyUser = userRepository.findById(1L)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 유저가 작성한 모든 질문 조회
        List<Question> questions = questionRepository.findQuestionsByAuthor(user);

        // 각 질문의 작성자를 dummyUser로 변경
        for (Question question : questions) {
            question.setAuthor(dummyUser);
        }

        // 유저가 작성한 모든 답변 조회
        List<Answer> answers = answerRepository.findAnswersByAuthor(user);

        // 각 답변의 작성자를 dummyUser로 변경
        for (Answer answer : answers) {
            answer.setAuthor(dummyUser);
        }

        // 유저가 작성한 모든 매칭 포스트 리뷰 조회
        List<MatchingPostReview> matchingPostReviews = matchingPostReviewRepository.findMatchingPostReviewsByReviewAuthor(
                user);

        // 각 매칭 포스트 리뷰의 작성자를 dummyUser로 변경
        for (MatchingPostReview review : matchingPostReviews) {
            review.setReviewAuthor(dummyUser);
        }

        // 유저가 작성한 모든 게시글 조회
        List<MatchingPost> matchingPosts = matchingPostRepository.findMatchingPostByUser(user);

        // 각 게시글의 작성자를 dummyUser로 변경
        for (MatchingPost post : matchingPosts) {
            post.setUser(dummyUser);
        }

        // 유저 삭제
        userRepository.delete(user);
    }

    public List<UserRankingDto> findUserRanking() {
        return userRepository.findUserByOrderByTotalExperience();
    }

    //닉네임 중복 여부
    public boolean isDuplicatedNickname(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    //학번 중복 여부
    public boolean isDuplicatedStudentNo(String studentNo) {
        return userRepository.existsByStudentNo(studentNo);
    }
}
