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
    public List<Response> getBothMessages(String email, Long selectedUserId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        User selectedUser = userRepository.findById(selectedUserId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        //서로 주고 받은 메세지가 삭제된 경우 삭제된 메세지는 보이지 않게 하기 위해 isDeleted 가 false 인 메세지만 조회
        //다른 상대에게 받은 메세지는 안뜨게 하고싶음.
        List<Message> messages = messageRepository.findAllBySenderAndReceiverAndIsDeletedFalseOrReceiverAndSenderAndIsDeletedFalse(
                user, selectedUser,
                user, selectedUser);
        return convertMessagesToDTO(messages);
    }

    @Transactional
    public List<MessageDTO.Preview> getAllMessagePreview(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        List<Message> messages = messageRepository.findAllByReceiverAndIsDeletedFalse(user);
        return convertMessagesToPreviewDTO(messages);
    }

    @Transactional
    public List<MessageDTO.Response> getMessagesByReceiver(String email) {
        User receiver = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        List<Message> messages = messageRepository.findAllByReceiverAndIsDeletedFalse(receiver);
        return convertMessagesToDTO(messages);
    }


    @Transactional
    public List<Response> getMessagesBySender(String email) {
        User sender = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        List<Message> messages = messageRepository.findAllBySenderAndIsDeletedFalse(sender);
        return convertMessagesToDTO(messages);
    }

    /**
     * 1 : 로그인한 사용자와 관련된 메세지인지 2 : sender 인지 receiver 인지 3 : isDeleted 가 false 인지 true 인지
     **/
    @Transactional
    public void deleteMessage(Long messageId, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new CustomException(ErrorCode.MESSAGE_NOT_FOUND));

        if (message.getSender().equals(user) || message.getReceiver().equals(user)) {
            if (message.getSender().equals(user)) {
                message.setDeleted(true);
            } else {
                message.setDeleted(true);
            }
        } else {
            throw new CustomException(ErrorCode.MESSAGE_NOT_FOUND);
        }
    }

    // 메세지 전체 삭제
    @Transactional
    public void deleteAllMessages(String email, Long selectedUserId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        User selectedUser = userRepository.findById(selectedUserId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        List<Message> messages = messageRepository.findAllBySenderAndReceiverAndIsDeletedFalseOrReceiverAndSenderAndIsDeletedFalse(
                user, selectedUser,
                user, selectedUser);
        for (Message message : messages) {
            message.setDeleted(true);
        }
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

