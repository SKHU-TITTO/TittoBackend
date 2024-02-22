package com.example.titto_backend.auth.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

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