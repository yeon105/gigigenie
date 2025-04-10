package com.gigigenie.domain.member.controller;

import com.gigigenie.domain.member.dto.EmailCheckResponseDTO;
import com.gigigenie.domain.member.dto.LoginRequestDTO;
import com.gigigenie.domain.member.dto.SignupRequestDTO;
import com.gigigenie.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO request) {
        return ResponseEntity.ok(memberService.login(request));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequestDTO request) {
        return ResponseEntity.ok(memberService.signup(request));
    }

    @GetMapping("/check-email")
    public ResponseEntity<?> checkEmail(@RequestParam String email) {
        return ResponseEntity.ok(new EmailCheckResponseDTO(memberService.checkEmail(email)));
    }
}
