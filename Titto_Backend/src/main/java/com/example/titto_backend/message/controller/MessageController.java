package com.example.titto_backend.message.controller;

import com.example.titto_backend.message.dto.MessageDTO;
import com.example.titto_backend.message.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/message", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Message Controller", description = "메시지 관련 API")
public class MessageController {

    private final MessageService messageService;

    @PostMapping("/write")
    @Operation(
            summary = "메시지 작성",
            description = "메시지를 작성합니다.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "요청 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "500", description = "관리자 문의")
            })
    public ResponseEntity<String> writeMessage(@RequestBody MessageDTO.Request request,
                                               @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(messageService.writeMessage(request, userDetails.getUsername()));
    }

    //쪽지함 전체 조회
    @GetMapping("/all")
    @Operation(
            summary = "전체 메시지 조회",
            description = "전체 메시지를 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "500", description = "관리자 문의")
            })
    public ResponseEntity<List<MessageDTO.Preview>> getAllMessagePreview(
            @AuthenticationPrincipal UserDetails userDetails) {
        return new ResponseEntity<>(messageService.getAllMessagePreview(userDetails.getUsername()), HttpStatus.OK);
    }

    //쪽지함 세부 조회 ( 주고 받은 사용자와의 대화 내용을 뿌려줄 수 있는 api)
    @GetMapping("/{selectedUserId}")
    @Operation(
            summary = "메시지 세부 조회",
            description = "메시지 세부를 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "500", description = "관리자 문의")
            })
    public ResponseEntity<List<MessageDTO.Response>> getBothMessages(
            @PathVariable Long selectedUserId,
            @AuthenticationPrincipal UserDetails userDetails) {
        return new ResponseEntity<>(messageService.getBothMessages(userDetails.getUsername(), selectedUserId),
                HttpStatus.OK);
    }

    @GetMapping("/receiver")
    @Operation(
            summary = "받은 메시지 조회",
            description = "받은 메시지를 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "500", description = "관리자 문의")
            })
    public ResponseEntity<List<MessageDTO.Response>> getMessagesByReceiver(
            @AuthenticationPrincipal UserDetails userDetails) {
        return new ResponseEntity<>(messageService.getMessagesByReceiver(userDetails.getUsername()), HttpStatus.OK);
    }

    @GetMapping("/sender")
    @Operation(
            summary = "보낸 메시지 조회",
            description = "보낸 메시지를 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "500", description = "관리자 문의")
            })
    public ResponseEntity<List<MessageDTO.Response>> getMessagesBySender(
            @AuthenticationPrincipal UserDetails userDetails) {
        return new ResponseEntity<>(messageService.getMessagesBySender(userDetails.getUsername()), HttpStatus.OK);
    }

    @PutMapping("/delete-all/{seletedUserId}")
    @Operation(
            summary = "전체 메시지 삭제",
            description = "선택한 유저와의 전체 메시지를 삭제합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "500", description = "관리자 문의")
            })
    public String deleteAllMessage(
            @PathVariable Long seletedUserId,
            @AuthenticationPrincipal UserDetails userDetails) {
        messageService.deleteAllMessages(userDetails.getUsername(), seletedUserId);
        return "메시지 전체 삭제 성공";
    }

}

