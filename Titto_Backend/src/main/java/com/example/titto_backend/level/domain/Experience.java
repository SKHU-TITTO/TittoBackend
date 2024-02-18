package com.example.titto_backend.level.domain;

import com.example.titto_backend.auth.domain.User;
import jakarta.persistence.*;

public class Experience {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "experience")
    private User user;

    @Column(name = "total_experience")
    private int totalExperience; // 사용자의 누적 경험치

    @Column(name = "current_experience")
    private int currentExperience; // 사용자의 현재 경험치

    @Column(name = "required_experience")
    private int requiredExperience;
}
