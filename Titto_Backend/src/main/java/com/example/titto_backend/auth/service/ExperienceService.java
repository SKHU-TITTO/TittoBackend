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
    private final BadgeService badgeService;

    // 경험치 추가
    @Transactional
    public void addExperience(User questionAuthor, User answerAuthor, int experienceToAdd) {
        if (questionAuthor != answerAuthor) {

            int currentExperience = answerAuthor.getCurrentExperience();
            int totalExperience = answerAuthor.getTotalExperience();

            currentExperience += experienceToAdd;
            totalExperience += experienceToAdd;

            answerAuthor.setCurrentExperience(currentExperience);
            answerAuthor.setTotalExperience(totalExperience);
        } else {
            throw new CustomException(ErrorCode.CANNOT_ACCEPTED);
        }
    }

    // 경험치 차감
    @Transactional
    public void deductExperience(User user, Integer deductedExperience) {
        Integer currentExperience = user.getCurrentExperience();

        if (deductedExperience > currentExperience) {
            throw new CustomException(ErrorCode.INSUFFICIENT_EXPERIENCE);
        }

        Integer newCurrentExperience = currentExperience - deductedExperience;
        user.setCurrentExperience(newCurrentExperience);
    }

    @Transactional
    public void levelUp(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        Integer level = user.getLevel();

        if (level < 5) {
            user.setLevel(level + 1);
        }
        badgeService.getTittoAuthorityBadge(user);
    }
}




