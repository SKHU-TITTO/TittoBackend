package com.example.titto_backend.message.repository;

import com.example.titto_backend.auth.domain.User;
import com.example.titto_backend.message.domain.Message;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findAllByReceiver(User user);

    List<Message> findAllBySender(User user);
}
