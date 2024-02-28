package com.example.titto_backend.message.repository;

import com.example.titto_backend.auth.domain.User;
import com.example.titto_backend.message.domain.Message;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findAllByReceiver(User user);

    List<Message> findAllBySender(User user);

    // user id를 받고 receiver이거나 sender인 메시지를 조회
    List<Message> findAllByReceiverOrSender(User receiver, User sender);

    // selectedUserId를 받고 자신과 서로 메시지를 주고받은 메시지를 조회
    List<Message> findAllBySenderAndReceiverOrReceiverAndSender(User sender, User receiver, User receiver2,
                                                                User sender2);
    // isDeleted가 false인 메시지만 조회
}
