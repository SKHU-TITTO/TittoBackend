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
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND)); //보낸사람 찾기

        User receiver = userRepository.findByNickname(request.getReceiverNickname())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND)); // 받는사람 찾기

        messageRepository.save(Message.builder()
                .sender(sender)
                .receiver(receiver)
                .senderNickname(sender.getNickname())
                .receiverNickname(receiver.getNickname())
                .content(request.getContent())
                .build());

        return "메시지 전송 성공";
    }

    // 서로 주고 받은 메세지
    @Transactional
    public List<Response> getBothMessages(String email, Long selectedUserId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        User selectedUser = userRepository.findById(selectedUserId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        List<Message> messages = messageRepository.findBySenderAndReceiverAndDeletedBySenderFalseOrReceiverAndSenderAndDeletedByReceiverFalseOrderBySentAtDesc(
                user, selectedUser, user, selectedUser);

        return convertMessagesToDTO(messages);
    }

    // 받은 메세지 미리보기
    @Transactional
    public List<MessageDTO.Preview> getAllMessagePreview(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        List<Message> messages = messageRepository.findAllByReceiverAndDeletedByReceiverFalse(user);
        return convertMessagesToPreviewDTO(messages);
    }

    // 받은 메세지함 조회
    @Transactional
    public List<Response> getMessagesByReceiver(String email) {
        User receiver = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        List<Message> messages = messageRepository.findAllByReceiverAndDeletedByReceiverFalse(receiver);
        return convertMessagesToDTO(messages);
    }

    // 보낸 메세지함 조회
    @Transactional
    public List<Response> getMessagesBySender(String email) {
        User sender = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        List<Message> messages = messageRepository.findAllBySenderAndDeletedBySenderFalse(sender);
        return convertMessagesToDTO(messages);
    }

    // 메세지 삭제
    @Transactional
    public void deleteAllMessages(String email, Long selectedUserId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        User selectedUser = userRepository.findById(selectedUserId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        List<Message> messages = messageRepository.findBySenderAndReceiverAndDeletedBySenderFalseOrReceiverAndSenderAndDeletedByReceiverFalseOrderBySentAtDesc(user,selectedUser,user,selectedUser);

        for (Message message : messages) {
            if (message.getSender().equals(user)) {
                message.setDeletedBySender(true);
            }
            if (message.getReceiver().equals(user)) {
                message.setDeletedByReceiver(true);
            }
        }

        // TODO:만약 둘다 삭제 시 데이터 베이스에서 지워지게 하기
    }

    private List<MessageDTO.Response> convertMessagesToDTO(List<Message> messages) {
        return messages.stream()
                .map(MessageDTO.Response::new)
                .toList();
    }

    private List<MessageDTO.Preview> convertMessagesToPreviewDTO(List<Message> messages) {
        return messages.stream()
                .map(MessageDTO.Preview::new)
                .toList();
    }

}

