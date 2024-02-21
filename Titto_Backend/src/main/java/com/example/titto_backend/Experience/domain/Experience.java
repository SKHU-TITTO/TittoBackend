package com.example.titto_backend.level.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Experience {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "total_experience")
    private Integer totalExperience = 0; // 사용자의 누적 경험치

    @Column(name = "current_experience")
    private Integer currentExperience = 0; // 사용자의 현재 경험치


    public void setCurrentExperience(int newCurrentExperience) {
        this.currentExperience = newCurrentExperience;
    }

    public void setTotalExperience(int newTotalExperience) {
        this.totalExperience = newTotalExperience;
    }

}
