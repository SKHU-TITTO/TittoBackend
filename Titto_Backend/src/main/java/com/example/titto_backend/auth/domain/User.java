package com.example.titto_backend.auth.domain;

import com.example.titto_backend.auth.dto.request.SignUpDTO;
import com.example.titto_backend.common.BaseEntity;
import com.example.titto_backend.feedback.domain.Feedback;
import com.example.titto_backend.matchingBoard.domain.matchingBoard.MatchingPost;
import com.example.titto_backend.matchingBoard.domain.review.MatchingPostReview;
import com.example.titto_backend.message.domain.Message;
import com.example.titto_backend.questionBoard.domain.Answer;
import com.example.titto_backend.questionBoard.domain.Question;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "profile", columnDefinition = "TEXT", nullable = true)
    private String profile;

    @Setter
    @Column(name = "name")
    private String name;

    @Setter
    @Column(name = "nickname")
    private String nickname;

    @Setter
    @Column(name = "student_no")
    private String studentNo;

    @Setter
    @Column(name = "department")
    private String department;

    @Column(name = "social_id")
    private String socialId;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    @Setter
    @Column(name = "one_line_intro")
    private String oneLineIntro;

    @Setter
    @Column(name = "self_intro", columnDefinition = "TEXT")
    private String selfIntro;

    @Setter
    @Enumerated(EnumType.STRING)
    @ElementCollection
    @Column(name = "badge")
    private Set<BadgeType> badges;

    @JsonIgnore
    @OneToMany(mappedBy = "author", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<Question> questions;

    @JsonIgnore
    @OneToMany(mappedBy = "author", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<Answer> answers;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<MatchingPost> matchingPosts;

    @JsonIgnore
    @OneToMany(mappedBy = "reviewAuthor", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<MatchingPostReview> matchingPostReviews;

    @JsonIgnore
    @OneToMany(mappedBy = "sender", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<Message> sentMessages;

    @JsonIgnore
    @OneToMany(mappedBy = "receiver", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<Message> receivedMessages;

    @JsonIgnore
    @OneToMany(mappedBy = "feedbackUser", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<Feedback> feedbacks;

    @Setter
    @Column(name = "total_experience")
    private Integer totalExperience; // 사용자의 누적 경험치

    @Setter
    @Column(name = "current_experience")
    private Integer currentExperience; // 사용자의 현재 경험치

    @Setter
    @Column(name = "count_question")
    private Integer countQuestion; // 질문 개수

    @Setter
    @Column(name = "count_answer")
    private Integer countAnswer;  // 답변 개수

    @Setter
    @Column(name = "count_accept")
    private Integer countAccept;  // 채택 개수

    @Setter
    @Column(name = "level")
    private Integer level; // 사용자의 레벨

    @Builder
    public User(String email, String profile, String socialId, SocialType socialType) {
        this.email = email;
        this.profile = profile;
        this.socialId = socialId;
        this.socialType = socialType;
    }

    public void signupUser(SignUpDTO signUpDTO) {
        this.setName(signUpDTO.getName());
        this.setNickname(signUpDTO.getNickname());
        this.setStudentNo(signUpDTO.getStudentNo());
        this.setDepartment(signUpDTO.getDepartment());
        this.setTotalExperience(0);
        this.setCurrentExperience(0);
        this.setCountQuestion(0);
        this.setCountAnswer(0);
        this.setCountAccept(0);
        this.setLevel(1);
    }
}
