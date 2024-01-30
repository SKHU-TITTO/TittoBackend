package com.example.titto_backend.auth.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

//사용자 닉네임 수정 요청
@Getter
@AllArgsConstructor
public class ModifyRequest {
    private String nickname;
}
