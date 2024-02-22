package com.example.titto_backend.message.dto;

import com.example.titto_backend.message.domain.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDTO {

    private String title;
    private String content;
    private Long receiverId;

    public static MessageDTO toDto(Message message) {
        return new MessageDTO(
                message.getTitle(),
                message.getContent(),
                message.getReceiver().getId()
        );
    }

}
