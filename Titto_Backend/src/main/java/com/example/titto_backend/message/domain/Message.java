package com.example.titto_backend.message.domain;

import com.example.titto_backend.auth.domain.User;
import com.example.titto_backend.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "messages")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Message extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private User receiver;

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "sent_time")
    private LocalDateTime sentAt;

    @Column(name = "sender_nickname")
    private String senderNickname;

    @Column(name = "receiver_nickname")
    private String receiverNickname;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    @Builder
    public Message(User sender, User receiver, String content, String senderNickname, String receiverNickname) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.senderNickname = senderNickname;
        this.receiverNickname = receiverNickname;
        this.sentAt = LocalDateTime.now();
    }
}