package com.example.titto_backend.domain.matchingBoard;

import jakarta.persistence.*;
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
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "matchingBoard_id")
    private Integer matchingBoardId;

    // 1대1 관계 매핑
    @OneToOne
    @JoinColumn(name = "matchingPost_id", unique = true, nullable = false)
    private MatchingPost matchingPost;

}
