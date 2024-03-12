package com.example.titto_backend.questionBoard.dto;

import com.example.titto_backend.questionBoard.domain.Answer;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class AnswerDTO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "질문 글에 댓글 작성")
    public static class Request {

        @Schema(description = "질문 ID")
        private Long questionId;

        @Schema(description = "답변 내용")
        @NotBlank
        private String content;
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "질문 글에 댓글 조회")
    public static class Response {
        @Schema(description = "답변 ID")
        private Long id;

        @Schema(description = "질문 ID")
        private Long postId;

        @Schema(description = "답변 작성자 ID")
        private String authorId;

        @Schema(description = "답변 작성자 닉네임")
        private String authorNickname;

        @Schema(description = "답변 내용")
        private String content;

        @Schema(description = "채택 여부", defaultValue = "false")
        private boolean isAccepted;

        @Schema(description = "프로필")
        private String profile;

        @Schema(description = "생성 날짜")
        private LocalDateTime createDate;

        @Schema(description = "수정 날짜")
        private LocalDateTime updateDate;

        @Schema(description = "사용자 레벨")
        private Integer level;

        public Response(Answer answer) {
            this.id = answer.getId();
            this.postId = answer.getQuestion().getId();
            this.authorId = answer.getAuthor().getId().toString();
            this.authorNickname = answer.getAuthor().getNickname();
            this.content = answer.getContent();
            this.isAccepted = answer.isAccepted();
            this.profile = answer.getAuthor().getProfile();
            this.createDate = answer.getCreateDate();
            this.updateDate = answer.getUpdateDate();
            this.level = answer.getAuthor().getLevel();
        }
    }
}
