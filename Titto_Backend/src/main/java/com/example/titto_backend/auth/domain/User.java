package com.example.titto_backend.auth.domain;

import com.example.titto_backend.auth.dto.request.SignUpDTO;
import com.example.titto_backend.common.BaseEntity;
import com.example.titto_backend.matchingBoard.domain.matchingBoard.MatchingPost;
import com.example.titto_backend.matchingBoard.domain.review.MatchingPostReview;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
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
  @NotBlank(message = "이메일은 필수!")
  private String email;

  //profile image
  @Column(name = "profile", nullable = true)
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

//  private Level level;
//  //post count
//  private Integer postCount;
//  //badge
//  private Badge badge;
//  //available point
//  private Integer availablePoint;

  @JsonIgnore
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<MatchingPost> matchingPosts;

  @JsonIgnore
  @OneToMany(mappedBy = "reviewAuthor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<MatchingPostReview> matchingPostReviews;

  @Builder
  public User(String email, String profile, String socialId) {
    this.email = email;
    this.profile = profile;
    this.socialId = socialId;
  }

  public void signupUser(SignUpDTO signUpDTO) {
    this.setName(signUpDTO.getName());
    this.setNickname(signUpDTO.getNickname());
    this.setStudentNo(signUpDTO.getStudentNo());
    this.setDepartment(signUpDTO.getDepartment());
  }

  public void updateNicknameAndStudentNo(String newNickname, String newStudentNo) {
    if (newNickname != null) {
      this.setNickname(newNickname);
    }

    if (newStudentNo != null) {
      this.setStudentNo(newStudentNo);
    }
  }
}
