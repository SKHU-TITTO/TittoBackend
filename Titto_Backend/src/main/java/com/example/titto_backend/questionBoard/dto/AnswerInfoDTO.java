package com.example.titto_backend.questionBoard.dto;

import com.example.titto_backend.questionBoard.domain.Department;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "사용자 작성 답글 조회")
public class AnswerInfoDTO {
    @Schema(description = "답글 ID")
    private Long id;

    @Schema(description = "답글 내용")
    private String content;

    @Schema(description = "질문 ID")
    private Long questionId;

    @Schema(description = "질문 제목")
    private String questionTitle;

    @Schema(description = "학과")
    private Department department;
}