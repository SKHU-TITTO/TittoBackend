package com.example.titto_backend.message.service;

import com.example.titto_backend.auth.domain.User;
import com.example.titto_backend.auth.repository.UserRepository;
import com.example.titto_backend.common.exception.CustomException;
import com.example.titto_backend.common.exception.ErrorCode;
import com.example.titto_backend.message.domain.Message;
import com.example.titto_backend.message.dto.MessageDTO;
import com.example.titto_backend.message.repository.MessageRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    @Transactional
    public void writeMessage(MessageDTO messageDTO, String email) {
        User sender = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        User receiver = userRepository.findById(messageDTO.getReceiverId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        messageRepository.save(Message.builder()
                .sender(sender)
                .receiver(receiver)
                .title(messageDTO.getTitle())
                .content(messageDTO.getContent())
                .build());
    }

    @Transactional
    public List<MessageDTO> getMessagesByReceiver(String email) {
        User receiver = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        List<Message> messages = messageRepository.findAllByReceiver(receiver);
        return convertMessagesToDTO(messages);
    }

    @Transactional
    public List<MessageDTO> getMessagesBySender(String email) {
        User sender = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        List<Message> messages = messageRepository.findAllBySender(sender);
        return convertMessagesToDTO(messages);
    }

    private List<MessageDTO> convertMessagesToDTO(List<Message> messages) {
        return messages.stream().map(MessageDTO::toDto).collect(Collectors.toList());
    }

}

