package com.example.titto_backend.auth.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserProfileUpdateDTO {
  private String oneLineIntro;
  private String selfIntro;
}
