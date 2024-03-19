package com.example.titto_backend.message.service;

import com.example.titto_backend.auth.domain.User;
import com.example.titto_backend.auth.repository.UserRepository;
import com.example.titto_backend.common.exception.CustomException;
import com.example.titto_backend.common.exception.ErrorCode;
import com.example.titto_backend.message.domain.Message;
import com.example.titto_backend.message.dto.MessageDTO;
import com.example.titto_backend.message.repository.MessageRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Comparator;

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

    // 사용자의 주고 받은 메시지 조회
    @Transactional
    public List<MessageDTO.Response> getBothMessages(String currentUserEmail, Long selectedUserId) {
        // 현재 사용자 조회
        User currentUser = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 선택된 사용자 조회
        User selectedUser = userRepository.findById(selectedUserId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        List<Message> sentMessages;
        List<Message> receivedMessages;

        // 현재 사용자가 보낸 메시지와 받은 메시지를 구분하여 조회
        if (currentUser.getId().equals(selectedUserId)) {
            // 현재 사용자가 선택한 대상에게 보낸 메시지 조회
            sentMessages = messageRepository.findBySenderAndReceiverAndDeletedBySenderFalse(currentUser, selectedUser);
            // 현재 사용자가 선택한 대상으로부터 받은 메시지 조회
            receivedMessages = messageRepository.findBySenderAndReceiverAndDeletedBySenderFalse(selectedUser,
                    currentUser);
        } else {
            // 현재 사용자가 선택한 대상에게 받은 메시지 조회
            receivedMessages = messageRepository.findBySenderAndReceiverAndDeletedBySenderFalse(currentUser,
                    selectedUser);
            // 현재 사용자가 선택한 대상에게 보낸 메시지 조회
            sentMessages = messageRepository.findBySenderAndReceiverAndDeletedBySenderFalse(selectedUser, currentUser);
        }

        // 조회된 메시지를 DTO로 변환하여 반환
        List<MessageDTO.Response> messagesDTO = new ArrayList<>();
        messagesDTO.addAll(convertMessagesToDTO(sentMessages));
        messagesDTO.addAll(convertMessagesToDTO(receivedMessages));

        // 메시지를 보낸 시간을 기준으로 정렬
        messagesDTO.sort(Comparator.comparing(MessageDTO.Response::getSentAt).reversed());

        return messagesDTO;
    }

    // 서로 주고 받은 메세지 조회
//    @Transactional
//    public List<MessageDTO.Response> getBothMessages(String email, Long selectedUserId) {
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
//
//        User selectedUser = userRepository.findById(selectedUserId)
//                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
//
//        List<Message> messages = messageRepository.findBySenderAndReceiverAndDeletedBySenderFalseOrReceiverAndSenderAndDeletedByReceiverFalseOrderBySentAtDesc(
//                user, selectedUser, user, selectedUser);
//
//        return convertMessagesToDTO(messages);
//    }

    // 메세지함에서 사용자 별 가장 최근 메세지만 보내줌.
    @Transactional
    public Map<User, Message> getUserConversations(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        List<Message> userMessages = messageRepository.findBySenderOrReceiverOrderBySentAtDesc(user, user);
        Map<User, Message> conversations = new HashMap<>();

        for (Message message : userMessages) {
            User otherUser = message.getSender().equals(user) ? message.getReceiver() : message.getSender();
            // 이미 대화 목록에 있는 사용자인 경우 최신 메시지로 갱신
            if (conversations.containsKey(otherUser)) {
                Message currentMessage = conversations.get(otherUser);
                if (message.getSentAt().isAfter(currentMessage.getSentAt())) {
                    conversations.put(otherUser, message);
                }
            } else {
                conversations.put(otherUser, message);
            }
        }
        return conversations;
    }

    // 받은 메세지함 조회
    @Transactional
    public List<MessageDTO.Response> getMessagesByReceiver(String email) {
        User receiver = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        List<Message> messages = messageRepository.findAllByReceiverAndDeletedByReceiverFalse(receiver);
        return convertMessagesToDTO(messages);
    }

    // 보낸 메세지함 조회
    @Transactional
    public List<MessageDTO.Response> getMessagesBySender(String email) {
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

        List<Message> messages = messageRepository.findBySenderAndReceiverAndDeletedBySenderFalseOrReceiverAndSenderAndDeletedByReceiverFalseOrderBySentAtDesc(
                user, selectedUser, user, selectedUser);

        for (Message message : messages) {
            if (message.getSender().equals(user)) {
                message.setDeletedBySender(true);
            }
            if (message.getReceiver().equals(user)) {
                message.setDeletedByReceiver(true);
            }
        }

        if (messages.stream().allMatch(message -> message.isDeletedBySender() && message.isDeletedByReceiver())) {
            messageRepository.deleteAll(messages);
        }
    }

    private List<MessageDTO.Response> convertMessagesToDTO(List<Message> messages) {
        return messages.stream()
                .map(MessageDTO.Response::new)
                .toList();
    }

}

