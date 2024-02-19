package com.example.titto_backend.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {

  /* 400 BAD_REQUEST : 잘못된 요청 */
  INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "입력 값이 잘못되었습니다."),
  INVALID_KAKAO_VALUE(HttpStatus.BAD_REQUEST, "잘못된 요청으로 카카오 서버의 응답을 받지 못했습니다."),
  INVALID_NAVER_VALUE(HttpStatus.BAD_REQUEST, "잘못된 요청으로 네이버 서버의 응답을 받지 못했습니다."),
  DUPLICATED_EMAIL(HttpStatus.BAD_REQUEST, "중복된 이메일입니다."),
  DUPLICATED_NICKNAME(HttpStatus.BAD_REQUEST, "중복된 닉네임입니다."),
  DUPLICATED_STUDENT_NO(HttpStatus.BAD_REQUEST, "중복된 학번입니다."),

  /* 401 UNAUTHORIZED : 인증되지 않은 사용자 */
  INVALID_AUTH_TOKEN(HttpStatus.UNAUTHORIZED, "인증 토큰이 유효하지 않습니다."),
  INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "리프레시 토큰이 유효하지 않습니다."),

  /* 404 NOT_FOUND : 리소스를 찾을 수 없음 */
  USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자의 정보를 찾을 수 없습니다."),
  USER_NOT_MATCH(HttpStatus.NOT_FOUND, "사용자의 정보가 일치하지 않습니다."),
  EMAIL_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자의 이메일을 찾을 수 없습니다."),
  PROFILE_IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자의 프로필 이미지를 찾을 수 없습니다."),

  QUESTION_NOT_FOUND(HttpStatus.NOT_FOUND, "질문을 찾을 수 없습니다."),
  ANSWER_NOT_FOUND(HttpStatus.NOT_FOUND, "답변을 찾을 수 없습니다."),
  MISMATCH_AUTHOR(HttpStatus.BAD_REQUEST, "권한이 없습니다."),
  ALREADY_ACCEPTED_ANSWER(HttpStatus.BAD_REQUEST, "이미 채택된 답변이 존재합니다.");

  private final HttpStatus httpStatus;
  private final String message;

}