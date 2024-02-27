package com.example.titto_backend.auth.dto.response;

import com.example.titto_backend.auth.domain.User;
import com.example.titto_backend.matchingBoard.domain.matchingBoard.MatchingPost;
import com.example.titto_backend.questionBoard.domain.Answer;
import com.example.titto_backend.questionBoard.domain.Question;
import java.util.List;
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
    private Integer countAnswer;
    private Integer countAccept;
    private Integer level;
    private List<MatchingPost> matchingPosts;
    private List<Question> questions;
    private List<Answer> answers;

    public static UserProfileViewDto of(User user, List<MatchingPost> matchingPosts, List<Question> questions,
                                        List<Answer> answers) {
        return new UserProfileViewDto(
                user.getProfile(),
                user.getName(),
                user.getNickname(),
                user.getStudentNo(),
                user.getDepartment(),
                user.getOneLineIntro(),
                user.getSelfIntro(),
                user.getBadges().toString(),
                user.getTotalExperience(),
                user.getCurrentExperience(),
                user.getCountAnswer(),
                user.getCountAccept(),
                user.getLevel(),
                matchingPosts,
                questions,
                answers
        );
    }
}
