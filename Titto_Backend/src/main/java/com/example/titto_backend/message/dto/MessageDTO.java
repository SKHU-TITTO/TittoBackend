package com.example.titto_backend.message.dto;

import com.example.titto_backend.message.domain.Message;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MessageDTO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Schema(description = "메시지 작성")
    public static class Request {
        @Schema(description = "내용")
        private String content;
        @Schema(description = "받는 사람 닉네임")
        private String receiverNickname;
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "메시지 조회")
    public static class Response {
        @Schema(description = "메시지 ID")
        private Long id;

        @Schema(description = "내용")
        private String content;

        @Schema(description = "보낸 사람 ID")
        private Long senderId;

        @Schema(description = "받는 사람 ID")
        private Long receiverId;

        @Schema(description = "보낸 시간")
        private String sentAt;

        @Schema(description = "받는 사람 닉네임")
        private String receiverNickname;

        @Schema(description = "보낸 사람 닉네임")
        private String senderNickname;

        public Response(Message message) {
            this.id = message.getId();
            this.content = message.getContent();
            this.senderId = message.getSender().getId();
            this.receiverId = message.getReceiver().getId();
            this.sentAt = message.getSentAt().toString();
            this.receiverNickname = message.getReceiverNickname();
            this.senderNickname = message.getSenderNickname();
        }
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "메세지함 조회")
    public static class Preview {
        @Schema(description = "메시지 ID")
        private Long id;

        @Schema(description = "내용")
        private String content;

        @Schema(description = "보낸 사람 닉네임")
        private String senderNickname;

        @Schema(description = "보낸 시간")
        private String sentAt;

        public Preview(Message message) {
            this.id = message.getId();
            this.content = message.getContent();
            this.senderNickname = message.getSenderNickname();
            this.sentAt = message.getSentAt().toString();
        }
    }

}

