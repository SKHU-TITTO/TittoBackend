package com.example.titto_backend.auth.domain;

import jakarta.persistence.*;

@Entity
public class Badge {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "badge_id")
  private Long id;

  @Enumerated(EnumType.STRING)
  @Column(name = "badge_type")
  private BadgeType badgeType;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  public Badge(BadgeType badgeType) {
    this.badgeType = badgeType;
  }
  public Badge() {

  }
}