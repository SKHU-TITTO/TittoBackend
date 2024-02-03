package com.example.titto_backend.message.domain;

import com.example.titto_backend.auth.domain.User;
import com.example.titto_backend.common.BaseEntity;
import jakarta.persistence.CascadeType;
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

//TODO: 메세지 삭제 기능 추가 할거임?
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

  // FetchType.LAZY로 바꿔야할지 고민중
  @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @JoinColumn(name = "sender_id")
  private User sender;

  @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @JoinColumn(name = "receiver_id")
  private User receiver;

  @Column(name = "title", nullable = false, length = 100)
  private String title;

  @Column(name = "content", columnDefinition = "TEXT", nullable = false)
  private String content;

  @Column(name = "sent_time")
  private LocalDateTime sentAt;

  @Builder
  public Message(User sender, User receiver, String title, String content) {
    this.sender = sender;
    this.receiver = receiver;
    this.title = title;
    this.content = content;
    this.sentAt = LocalDateTime.now();
  }
}
