package com.example.titto_backend.auth.repository;

import com.example.titto_backend.auth.domain.User;
import com.example.titto_backend.auth.dto.response.UserRankingDto;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByNickname(String nickname);

    boolean existsByNickname(String nickname);

    boolean existsByStudentNo(String studentNo);

    @Query("SELECT new com.example.titto_backend.auth.dto.response.UserRankingDto("
            + "a.id, a.profile, a.nickname, a.studentNo, a.department, a.totalExperience, a.level)"
            + "FROM User a WHERE a.id NOT IN (1)"
            + "ORDER BY a.totalExperience DESC limit 10"
    )
    List<UserRankingDto> findUserByOrderByTotalExperience();
}