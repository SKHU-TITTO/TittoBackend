package com.example.titto_backend.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserRankingDto {
    private Long userId;
    private String profile;
    private String nickname;
    private String studentNo;
    private String department;
    private Integer totalExperience;
    private Integer level;
}
