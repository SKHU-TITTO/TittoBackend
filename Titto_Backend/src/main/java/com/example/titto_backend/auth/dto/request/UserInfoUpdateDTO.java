package com.example.titto_backend.auth.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfoUpdateDTO {
  private String newNickname;
  private String newStudentNo;

  public UserInfoUpdateDTO(String newNickname, String newStudentNo) {
    this.newNickname = newNickname;
    this.newStudentNo = newStudentNo;
  }
}
