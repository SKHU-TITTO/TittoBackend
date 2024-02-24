package com.example.titto_backend.matchingBoard.domain.matchingBoard;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchingBoard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "matchingBoard_id")
    private Integer matchingBoardId;

    // 1대1 관계 매핑
    @OneToOne
    @JoinColumn(name = "matchingPost_id", unique = true, nullable = false)
    private MatchingPost matchingPost;

    // 검색 키워드
    @Column(name = "key_word")
    private String keyWord;
}
