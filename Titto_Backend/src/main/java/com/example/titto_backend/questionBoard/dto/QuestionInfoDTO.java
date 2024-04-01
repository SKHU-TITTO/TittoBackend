package com.example.titto_backend.questionBoard.dto;

import com.example.titto_backend.questionBoard.domain.Department;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "사용자 작성 게시글 조회")
public class QuestionInfoDTO {

    @Schema(description = "질문 ID")
    private Long id;

    @Schema(description = "제목")
    private String title;

    @Schema(description = "내용")
    private String content;

    @Schema(description = "작성일")
    private LocalDateTime createdDate;

    @Schema(description = "조회수")
    private Integer viewCount;

    @Schema(description = "답변 개수")
    private Integer answerCount;

    @Schema(description = "학과")
    private Department department;

}
