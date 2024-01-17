package com.example.titto_backend.domain;

import com.example.titto_backend.domain.matchingBoard.MatchingPost;
import com.example.titto_backend.domain.review.Review;
import com.example.titto_backend.login.domain.oauth.OAuthProvider;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Builder
@Table(name = "users")
@AllArgsConstructor
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id")
  private Long id;

  private String email;

  private String image_url;

  private OAuthProvider oAuthProvider;

  private String nickname;

  private String studentNumber;

  private String department;

  private LocalDateTime createDate;

  private Level level;
  //post count
  private Integer postCount;
  //badge
  private Badge badge;
  //available point
  private Integer availablePoint;

  @Builder
  public User(String email, String image_url, OAuthProvider oAuthProvider) {
    this.email = email;
    this.image_url = image_url;
    this.oAuthProvider = oAuthProvider;
  }

  @JsonIgnore
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<MatchingPost> matchingPosts;

  @JsonIgnore
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<Review> reviews;
}
