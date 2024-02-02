package com.example.titto_backend.auth.repository;

import com.example.titto_backend.auth.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByEmail(String email);

  boolean existsByNickname(String nickname);

  boolean existsByStudentNo(String studentNo);
}
