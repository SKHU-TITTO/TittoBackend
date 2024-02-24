package com.example.titto_backend.auth.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserInfoUpdateDTO {
    private String newNickname;
}
