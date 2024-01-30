package com.example.titto_backend.auth.service;

import com.example.titto_backend.auth.domain.User;
import com.example.titto_backend.auth.dto.request.SignUpDTO;
import com.example.titto_backend.auth.dto.request.UserInfoUpdateDTO;
import com.example.titto_backend.auth.repository.UserRepository;
import com.example.titto_backend.common.exception.CustomException;
import com.example.titto_backend.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;

  @Transactional
  public void signUp(SignUpDTO signUpDTO, String email) {
    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    user.signupUser(signUpDTO);
  }

  @Transactional
  public void updateNicknameAndStudentNo(String email, UserInfoUpdateDTO requestDTO) {
    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    if (requestDTO.getNewNickname() != null && isDuplicatedNickname(requestDTO.getNewNickname())) {
      throw new CustomException(ErrorCode.DUPLICATED_NICKNAME);
    }

    if (requestDTO.getNewStudentNo() != null && isDuplicatedStudentNo(requestDTO.getNewStudentNo())) {
      throw new CustomException(ErrorCode.DUPLICATED_STUDENT_NO);
    }

    // 둘 다 null이거나 중복되지 않은 경우에만 업데이트 수행
    if (requestDTO.getNewNickname() != null) {
      user.setNickname(requestDTO.getNewNickname());
    }

    if (requestDTO.getNewStudentNo() != null) {
      user.setStudentNo(requestDTO.getNewStudentNo());
    }
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
