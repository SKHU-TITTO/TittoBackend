package com.example.titto_backend.auth.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserProfileUpdateDTO {
    private String oneLineIntro;
    private String selfIntro;
}
