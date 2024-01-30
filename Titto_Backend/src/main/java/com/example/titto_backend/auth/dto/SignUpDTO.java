package com.example.titto_backend.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class SignUpDTO {

  @NotBlank
  private String name;
  @NotBlank
  private String nickname;
  @NotBlank
  private String studentNo;
  @NotBlank
  private String department;
}
