package com.example.titto_backend.auth.controller;

import com.example.titto_backend.auth.dto.request.SignUpDTO;
import com.example.titto_backend.auth.dto.request.UserInfoUpdateDTO;
import com.example.titto_backend.auth.dto.request.UserProfileUpdateDTO;
import com.example.titto_backend.auth.dto.response.UserInfoDTO;
import com.example.titto_backend.auth.dto.response.UserProfileViewDto;
import com.example.titto_backend.auth.dto.response.UserRankingDto;
import com.example.titto_backend.auth.service.ExperienceService;
import com.example.titto_backend.auth.service.UserService;
import com.example.titto_backend.questionBoard.dto.AnswerInfoDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "User Controller", description = "유저 관련 API")
public class UserController {

    private final UserService userService;
    private final ExperienceService experienceService;

    @PutMapping("/signup")
    @PreAuthorize("isAuthenticated()")
    @Operation(
            summary = "회원가입",
            description = "회원가입을 진행합니다(카카오, 네이버 로그인 후 진행)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청 성공"),
                    @ApiResponse(responseCode = "500", description = "관리자 문의")
            })
    public String signUp(@RequestBody SignUpDTO signUpDTO, @AuthenticationPrincipal UserDetails userDetails) {
        userService.signUp(signUpDTO, userDetails.getUsername());
        return "success";
    }

    // 사용자 정보 불러오기
    @GetMapping("/info")
    @PreAuthorize("isAuthenticated()")
    @Operation(
            summary = "유저 정보 불러오기",
            description = "유저 정보를 불러옵니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "500", description = "관리자 문의")
            })
    public ResponseEntity<UserInfoDTO> getUserInfo(@AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        UserInfoDTO userInfo = userService.getUser(email);
        return ResponseEntity.ok(userInfo);
    }

    @PutMapping("/update")
    @PreAuthorize("isAuthenticated()")
    @Operation(
            summary = "닉네임 수정",
            description = "닉네임 수정이 가능합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "500", description = "관리자 문의")
            })
    public ResponseEntity<String> updateNicknameAndStudentNo(
            @RequestBody UserInfoUpdateDTO requestDTO,
            @AuthenticationPrincipal UserDetails userDetails) {
        String userEmail = userDetails.getUsername();
        userService.updateNickname(userEmail, requestDTO);
        return ResponseEntity.ok(" updated successfully");
    }

    @PutMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    @Operation(
            summary = "유저 프로필 수정",
            description = "유저 프로필을 수정합니다(한줄소개, 자기소개)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "500", description = "관리자 문의")
            })
    public ResponseEntity<String> updateUserProfile(@AuthenticationPrincipal UserDetails userDetails,
                                                    @RequestBody UserProfileUpdateDTO userProfileUpdateDTO) {
        String email = userDetails.getUsername();
        userService.updateUserProfile(email, userProfileUpdateDTO);
        return ResponseEntity.ok("updated profile");
    }

    @GetMapping("/check/nickname")
    @Operation(
            summary = "닉네임 중복 확인",
            description = "닉네임 중복 여부를 확인합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "500", description = "관리자 문의")
            })
    public ResponseEntity<String> checkNickname(@RequestParam String nickname) {
        if (userService.isDuplicatedNickname(nickname)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Nickname is already taken");
        } else {
            return ResponseEntity.ok("Nickname is available");
        }
    }

    @GetMapping("/check/studentNo")
    @Operation(
            summary = "학번 중복 확인",
            description = "학번 중복 여부를 확인합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "500", description = "관리자 문의")
            })
    public ResponseEntity<String> checkStudentNo(@RequestParam String studentNo) {
        if (userService.isDuplicatedStudentNo(studentNo)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Student number is already taken");
        } else {
            return ResponseEntity.ok("Student number is available");
        }
    }

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    @Operation(
            summary = "사용자 프로필 조회",
            description = "사용자의 프로필을 조회합니다",
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "500", description = "관리자 문의")
            })
    public ResponseEntity<UserProfileViewDto> getUserProfile(@RequestParam Long userId) {
        UserProfileViewDto userProfileViewDto = userService.userProfileView(userId);
        return new ResponseEntity<>(userProfileViewDto, HttpStatus.OK);
    }

    @GetMapping("/level/update")
    @Operation(
            summary = "사용자 레벨업",
            description = "사용자 레벨을 1 증가 시킵니다",
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "500", description = "관리자 문의")
            })
    public ResponseEntity<String> userLevelUp(@RequestParam Long userId) {
        experienceService.levelUp(userId);
        return ResponseEntity.ok("사용자 레벨업이 완료되었습니다");
    }

    @GetMapping("/posts/{userId}")
    @Operation(
            summary = "사용자 작성 글 보기",
            description = "사용자가 작성한 게시글을 볼 수 있습니다",
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "500", description = "관리자 문의")
            })
    public ResponseEntity<List<Object>> getUserPosts(@PathVariable Long userId) {
        List<Object> userPosts = userService.userPostsView(userId);
        return new ResponseEntity<>(userPosts, HttpStatus.OK);
    }

    @GetMapping("/answers/{userId}")
    @Operation(
            summary = "사용자 작성 답글 보기",
            description = "사용자가 작성한 답들을 볼 수 있습니다",
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "500", description = "관리자 문의")
            })
    public ResponseEntity<List<AnswerInfoDTO>> getUserAnswers(@PathVariable Long userId) {
        List<AnswerInfoDTO> userAnswers = userService.userAnswerView(userId);
        return new ResponseEntity<>(userAnswers, HttpStatus.OK);
    }

    // Delete
    @DeleteMapping("/{userId}")
    @PreAuthorize("isAuthenticated()")
    @Operation(
            summary = "회원 탈퇴",
            description = "회원 탈퇴를 진행합니다",
            responses = {
                    @ApiResponse(responseCode = "200", description = "회원 탈퇴 성공"),
                    @ApiResponse(responseCode = "404", description = "회원을 찾을 수 없음")
            })
    public ResponseEntity<String> deleteUser(@PathVariable("userId") Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok("회원 탈퇴가 완료되었습니다");
    }

    @GetMapping("/ranking")
    @Operation(
            summary = "회원 랭킹 조회",
            description = "회원 랭킹을 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공"),
                    @ApiResponse(responseCode = "404", description = "회원을 찾을 수 없음")
            })
    public ResponseEntity<List<UserRankingDto>> getUserRanking() {
        List<UserRankingDto> userRankingDtoList = userService.findUserRanking();
        return new ResponseEntity<>(userRankingDtoList, HttpStatus.OK);
    }
}
