package com.example.titto_backend.questionBoard.dto;

import com.example.titto_backend.questionBoard.domain.Department;
import com.example.titto_backend.questionBoard.domain.Question;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class QuestionDTO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Schema(description = "질문 글 작성")
    public static class Request {

        @Schema(description = "제목")
        @NotBlank
        private String title;

        @Schema(description = "내용")
        @NotBlank
        private String content;

        // 아직 미완성 상태
        @Schema(description = "이미지")
        private List<String> imageList;

        @Schema(description = "카테고리", example = "HUMANITIES, MANAGEMENT, SOCIETY, MEDIA_CONTENT, FUTURE_FUSION, SOFTWARE")
        @NotBlank
        private String department;

        @Schema(description = "상태", example = "SOLVED or UNSOLVED")
        @NotBlank
        private String status;

        @Schema(description = "줄 포인트")
        @NotBlank
        private Integer sendExperience;
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "질문 글 조회")
    public static class Response {

        @Schema(description = "질문 ID")
        private Long id;

        @Schema(description = "질문 작성자 ID")
        private Long authorId;

        @Schema(description = "질문 작성자 닉네임")
        private String authorNickname;

        @Schema(description = "카테고리")
        private String department;

        @Schema(description = "상태")
        private String status;

        @Schema(description = "제목")
        private String title;

        @Schema(description = "내용")
        private String content;

        @Schema(description = "작성일")
        private LocalDateTime createdDate;

        @Schema(description = "답변 목록")
        private List<AnswerDTO.Response> answerList;

        @Schema(description = "채택 여부")
        private boolean isAccepted;

        @Schema(description = "조회수")
        private Integer viewCount;

        @Schema(description = "생성 날짜")
        private LocalDateTime createDate;

        @Schema(description = "수정 날짜")
        private LocalDateTime updateDate;

        @Schema(description = "사용자 레벨")
        private Integer level;

        @Schema(description = "사용자 레벨")
        private String profile;

        @Schema(description = "줄 포인트")
        private Integer sendExperience;

        public Response(Question question) {
            this.id = question.getId();
            this.authorId = question.getAuthor().getId();
            this.authorNickname = question.getAuthor().getNickname();
            this.department = question.getDepartment().toString();
            this.status = question.getStatus().toString();
            this.title = question.getTitle();
            this.content = question.getContent();
            this.answerList = question.getAnswers().stream().map(AnswerDTO.Response::new).toList();
            this.isAccepted = question.getAcceptedAnswer() != null;
            this.viewCount = question.getViewCount();
            this.createDate = question.getCreateDate();
            this.updateDate = question.getUpdateDate();
            this.level = question.getAuthor().getLevel();
            this.profile = question.getAuthor().getProfile();
            this.sendExperience = question.getSendExperience();
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "질문 글 수정")
    public static class Update {
        @Schema(description = "제목")
        @NotBlank
        private String title;

        @Schema(description = "내용")
        @NotBlank
        private String content;

        @Schema(description = "카테고리")
        private Department department;
    }
}
