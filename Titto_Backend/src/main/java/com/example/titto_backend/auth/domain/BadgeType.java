package com.example.titto_backend.auth.domain;

// TODO: 어떤 뱃지 넣을 지 정하기
public enum BadgeType {
    // 신입 답변러, 초보 답변러, 견습 답변러, 프로 답변러, 전문 답변러
    NOVICE_RESPONDER, BEGINNER_RESPONDER, TRAINEE_RESPONDER, PROFESSIONAL_RESPONDER, EXPERT_RESPONDER,

    // 신입 질문러, 초보 질문러, 견습 질문러, 프로 질문러, 전문 질문러
    NOVICE_INQUIRER, BEGINNER_INQUIRER, TRAINEE_INQUIRER, PROFESSIONAL_INQUIRER, EXPERT_INQUIRER,

    // 신입 해결사, 초보 해결사, 견습 해결사, 프로 해결사, 전문 해결사
    NOVICE_SOLVER, BEGINNER_SOLVER, TRAINEE_SOLVER, PROFESSIONAL_SOLVER, EXPERT_SOLVER,

    // 이스터에그
    TITTO_MASTER, TITTO_AUTHORITY
}
