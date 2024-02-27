package com.example.titto_backend.auth.dto.response;

import com.example.titto_backend.auth.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserInfoDTO {
    private Long Id;
    private String ProfileImg;
    private String nickname;
    private String email;
    private String socialType;
    private Integer totalExperience;
    private Integer currentExperience;
    private Integer level;

    public UserInfoDTO(User user) {
        this.Id = user.getId();
        this.ProfileImg = user.getProfile();
        this.nickname = user.getNickname();
        this.email = user.getEmail();
        this.socialType = user.getSocialType().toString();
        this.totalExperience = user.getTotalExperience();
        this.currentExperience = user.getCurrentExperience();
        this.level = user.getLevel();
    }
}
