package com.example.titto_backend.message.repository;

import com.example.titto_backend.auth.domain.User;
import com.example.titto_backend.message.domain.Message;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findAllByReceiverAndIsDeletedFalse(User user);

    List<Message> findAllBySenderAndIsDeletedFalse(User user);

    // selectedUserId를 받고 자신과 서로 메시지를 주고받은 메시지를 조회
    List<Message> findAllBySenderAndReceiverAndIsDeletedFalseOrReceiverAndSenderAndIsDeletedFalse(User sender, User receiver, User receiver2, User sender2);


    // selectedUser와 주고 받은 메시지를 조회
    // 서로 주고 받은 메세지가 삭제된 경우 삭제된 메세지는 보이지 않게 하기 위해 isDeleted 가 false 인 메세지만 조회
    // 다른 상대에게 받은 메세지는 안뜨게 하고싶음.

    // isDeleted가 false인 메시지만 조회
    List<Message> findAllByIsDeletedFalse();
}
