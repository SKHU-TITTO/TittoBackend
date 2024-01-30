package com.example.titto_backend.auth.controller;

import com.example.titto_backend.auth.dto.SignUpDTO;
import com.example.titto_backend.auth.service.UserService;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;

  @PutMapping("/user")
  public String signUp(@RequestBody SignUpDTO signUpDTO, Principal principal) {
    userService.signUp(signUpDTO, principal.getName());
    return "success";
  }
}
