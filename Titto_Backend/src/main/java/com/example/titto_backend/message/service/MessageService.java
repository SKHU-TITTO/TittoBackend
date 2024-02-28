package com.example.titto_backend.message.service;

import com.example.titto_backend.auth.domain.User;
import com.example.titto_backend.auth.repository.UserRepository;
import com.example.titto_backend.common.exception.CustomException;
import com.example.titto_backend.common.exception.ErrorCode;
import com.example.titto_backend.message.domain.Message;
import com.example.titto_backend.message.dto.MessageDTO;
import com.example.titto_backend.message.dto.MessageDTO.Response;
import com.example.titto_backend.message.repository.MessageRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    @Transactional
    public String writeMessage(MessageDTO.Request request, String email) throws CustomException {
        User sender = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        User receiver = userRepository.findByNickname(request.getReceiverNickname())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        messageRepository.save(Message.builder()
                .sender(sender)
                .receiver(receiver)
                .senderNickname(sender.getNickname())
                .receiverNickname(receiver.getNickname())
                .content(request.getContent())
                .build());

        return "메시지 전송 성공";
    }

    @Transactional
    public List<Response> getMessagesByReceiver(String email) {
        User receiver = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        List<Message> messages = messageRepository.findAllByReceiver(receiver);
        return convertMessagesToDTO(messages);
    }

    @Transactional
    public List<Response> getMessagesBySender(String email) {
        User sender = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        List<Message> messages = messageRepository.findAllBySender(sender);
        return convertMessagesToDTO(messages);
    }

    private List<MessageDTO.Response> convertMessagesToDTO(List<Message> messages) {
        return messages.stream()
                .map(MessageDTO.Response::new)
                .toList();
    }
}

