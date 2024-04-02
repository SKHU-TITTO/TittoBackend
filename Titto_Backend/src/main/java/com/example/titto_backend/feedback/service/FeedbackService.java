package com.example.titto_backend.feedback.service;

import static java.util.stream.Collectors.toList;

import com.example.titto_backend.auth.domain.User;
import com.example.titto_backend.auth.repository.UserRepository;
import com.example.titto_backend.common.exception.CustomException;
import com.example.titto_backend.common.exception.ErrorCode;
import com.example.titto_backend.feedback.domain.Feedback;
import com.example.titto_backend.feedback.dto.FeedbackDTO;
import com.example.titto_backend.feedback.dto.FeedbackDTO.Response;
import com.example.titto_backend.feedback.repository.FeedbackRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final UserRepository userRepository;
    private final FeedbackRepository feedbackRepository;

    @Transactional
    public String writeFeedback(String email, FeedbackDTO.Request request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        feedbackRepository.save(Feedback.builder()
                .feedbackUser(user)
                .content(request.getContent())
                .build());

        return "문의사항이 성공적으로 등록되었습니다.";
    }

    @Transactional(readOnly = true)
    public List<Response> findAllFeedbacks() {
        List<Feedback> feedbacks = feedbackRepository.findAll();

        return feedbacks.stream()
                .map(feedback -> new Response(feedback.getId(), feedback.getFeedbackUser().getEmail(), feedback.getContent()))
                .collect(toList());
    }

}
