package com.example.titto_backend.feedback.dto;

import com.example.titto_backend.feedback.domain.Feedback;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class FeedbackDTO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        private String content;
    }

    @Data
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Response {
        private Long id;
        private String email;
        private String content;

        public Response(Feedback feedback) {
            this.id = feedback.getId();
            this.email = feedback.getFeedbackUser().getEmail();
            this.content = feedback.getContent();
        }
    }

}
