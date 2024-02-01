package com.example.titto_backend.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CustomException extends RuntimeException {
  private final ErrorCode errorCode;
}
