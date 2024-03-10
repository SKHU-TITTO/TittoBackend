package com.example.titto_backend.auth.service;

import com.example.titto_backend.auth.domain.User;
import com.example.titto_backend.auth.dto.request.SignUpDTO;
import com.example.titto_backend.auth.dto.request.UserInfoUpdateDTO;
import com.example.titto_backend.auth.dto.request.UserProfileUpdateDTO;
import com.example.titto_backend.auth.dto.response.UserInfoDTO;
import com.example.titto_backend.auth.dto.response.UserProfileViewDto;
import com.example.titto_backend.auth.repository.UserRepository;
import com.example.titto_backend.common.exception.CustomException;
import com.example.titto_backend.common.exception.ErrorCode;
import com.example.titto_backend.matchingBoard.repository.matchingBoard.MatchingPostRepository;
import com.example.titto_backend.questionBoard.dto.AnswerInfoDto;
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
    public List<AnswerInfoDto> userAnswerView(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return answerRepository.findAnswersInfoByAuthor(user);
    }


    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        userRepository.delete(user);
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
