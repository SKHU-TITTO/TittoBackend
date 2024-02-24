package com.example.titto_backend.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserProfileViewDto {
    private String profile;
    private String name;
    private String nickname;
    private String studentNo;
    private String department;
    private String oneLineIntro;
    private String selfIntro;
    private String badges;
    private Integer totalExperience;
    private Integer currentExperience;

}
