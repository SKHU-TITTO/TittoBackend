package com.example.titto_backend.questionBoard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnswerInfoDto {
    private Long id;
    private String content;

}
