package com.example.titto_backend.auth.service;

import com.example.titto_backend.auth.domain.User;
import com.example.titto_backend.auth.repository.UserRepository;
import com.example.titto_backend.common.exception.CustomException;
import com.example.titto_backend.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ExperienceService {
    private final UserRepository userRepository;

    // 경험치 추가
    @Transactional
    public void addExperience(User user, int experienceToAdd) {
        int currentExperience = user.getCurrentExperience();
        int totalExperience = user.getTotalExperience();

        currentExperience += experienceToAdd;
        totalExperience += experienceToAdd;

        user.setCurrentExperience(currentExperience);
        user.setTotalExperience(totalExperience);
    }

    // 경험치 차감
    @Transactional
    public void deductExperience(User user, int deductedExperience) {
        Integer currentExperience = user.getCurrentExperience();

        if (deductedExperience > currentExperience) {
            throw new CustomException(ErrorCode.INSUFFICIENT_EXPERIENCE);
        }

        int newCurrentExperience = currentExperience - deductedExperience;
        user.setCurrentExperience(newCurrentExperience);
    }
}




