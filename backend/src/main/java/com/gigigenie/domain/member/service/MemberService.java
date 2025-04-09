package com.gigigenie.domain.member.service;

import com.gigigenie.config.jwt.JwtTokenProvider;
import com.gigigenie.domain.member.dto.AuthResponseDTO;
import com.gigigenie.domain.member.dto.LoginRequestDTO;
import com.gigigenie.domain.member.dto.SignupRequestDTO;
import com.gigigenie.domain.member.entity.Member;
import com.gigigenie.domain.member.enums.MemberRole;
import com.gigigenie.domain.member.exception.EmailAlreadyExistsException;
import com.gigigenie.domain.member.exception.InvalidPasswordException;
import com.gigigenie.domain.member.exception.MemberNotFoundException;
import com.gigigenie.domain.member.repository.MemberRepository;
import com.gigigenie.domain.member.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public AuthResponseDTO login(LoginRequestDTO request) {
        ValidationUtil.validateEmail(request.getEmail());
        ValidationUtil.validatePassword(request.getPassword());

        Member member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new MemberNotFoundException(request.getEmail()));

        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new InvalidPasswordException();
        }

        String token = jwtTokenProvider.createToken(member.getMemberId());
        return new AuthResponseDTO(token, member);
    }

    @Transactional
    public AuthResponseDTO signup(SignupRequestDTO request) {
        ValidationUtil.validateEmail(request.getEmail());
        ValidationUtil.validatePassword(request.getPassword());

        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException(request.getEmail());
        }

        Member member = new Member();
        member.setMemberId(UUID.randomUUID().toString());
        member.setName(request.getName());
        member.setEmail(request.getEmail());
        member.setPassword(passwordEncoder.encode(request.getPassword()));
        member.setRole(MemberRole.USER);
        member.setJoinDate(LocalDateTime.now());

        memberRepository.save(member);
        String token = jwtTokenProvider.createToken(member.getMemberId());

        return new AuthResponseDTO(token, member);
    }

    public boolean checkEmail(String email) {
        ValidationUtil.validateEmail(email);
        return memberRepository.existsByEmail(email);
    }
} 