package com.example.titto_backend.auth.dto.response;

import com.example.titto_backend.auth.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserInfoDTO {
    private String ProfileImg;
    private String nickname;
    private String email;
    private String socialType;

    public UserInfoDTO(User user) {
        this.ProfileImg = user.getProfile();
        this.nickname = user.getNickname();
        this.email = user.getEmail();
        this.socialType = user.getSocialType().toString();
    }
}
