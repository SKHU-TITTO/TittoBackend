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
public class AnswerInfoDto {
    private Long id;
    private String content;
    private Long questionId;
    private String questionTitle;
    private Department department;
}