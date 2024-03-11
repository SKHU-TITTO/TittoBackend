package com.example.titto_backend.auth.service;

import com.example.titto_backend.auth.domain.BadgeType;
import com.example.titto_backend.auth.domain.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BadgeService {

    @Transactional
    public void getAnswerBadge(User user, int countAnswer) {
        List<BadgeType> badges = user.getBadges();
        addBadge(badges, countAnswer, BadgeType.NOVICE_RESPONDER, 1);
        addBadge(badges, countAnswer, BadgeType.BEGINNER_RESPONDER, 2, 5);
        addBadge(badges, countAnswer, BadgeType.TRAINEE_RESPONDER, 6, 10);
        addBadge(badges, countAnswer, BadgeType.PROFESSIONAL_RESPONDER, 11, 20);
        addBadge(badges, countAnswer, BadgeType.EXPERT_RESPONDER, 50);
        user.setBadges(badges);
    }

    public void getQuestionBadge(User user, int countQuestion) {
        List<BadgeType> badges = user.getBadges();
        addBadge(badges, countQuestion, BadgeType.NOVICE_INQUIRER, 1);
        addBadge(badges, countQuestion, BadgeType.BEGINNER_INQUIRER, 2, 5);
        addBadge(badges, countQuestion, BadgeType.TRAINEE_INQUIRER, 6, 10);
        addBadge(badges, countQuestion, BadgeType.PROFESSIONAL_INQUIRER, 11, 20);
        addBadge(badges, countQuestion, BadgeType.EXPERT_INQUIRER, 50);
        user.setBadges(badges);
    }

    public void getAcceptBadge(User user, int countAccept) {
        List<BadgeType> badges = user.getBadges();
        addBadge(badges, countAccept, BadgeType.NOVICE_SOLVER, 1);
        addBadge(badges, countAccept, BadgeType.BEGINNER_SOLVER, 2, 5);
        addBadge(badges, countAccept, BadgeType.TRAINEE_SOLVER, 6, 10);
        addBadge(badges, countAccept, BadgeType.PROFESSIONAL_SOLVER, 11, 20);
        addBadge(badges, countAccept, BadgeType.EXPERT_SOLVER, 50);
        user.setBadges(badges);
    }

    private void addBadge(List<BadgeType> badges, int count, BadgeType badgeType, int... counts) {
        boolean hasBadge = badges.contains(badgeType);
        if (!hasBadge) {
            for (int i = 0; i < counts.length; i += 2) {
                int minCount = counts[i];
                int maxCount = (i + 1 < counts.length) ? counts[i + 1] : Integer.MAX_VALUE;
                if (count >= minCount && count <= maxCount) {
                    badges.add(badgeType);
                    return;
                }
            }
        }
    }

}
