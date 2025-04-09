package com.gigigenie.domain.member.util;

import com.gigigenie.domain.member.exception.MemberException;

import java.util.regex.Pattern;

public class ValidationUtil {
    private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,}$";

    public static void validateEmail(String email) {
        if (email == null || email.isEmpty()) {
            throw new MemberException("이메일은 필수 입력값입니다.");
        }
        
        if (!Pattern.matches(EMAIL_PATTERN, email)) {
            throw new MemberException("올바른 이메일 형식이 아닙니다.");
        }
    }

    public static void validatePassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new MemberException("비밀번호는 필수 입력값입니다.");
        }
        
        if (password.length() < 8) {
            throw new MemberException("비밀번호는 최소 8자 이상이어야 합니다.");
        }
        
        if (!Pattern.matches(".*[0-9].*", password)) {
            throw new MemberException("비밀번호는 숫자를 포함해야 합니다.");
        }
        
        if (!Pattern.matches(".*[a-z].*", password)) {
            throw new MemberException("비밀번호는 소문자를 포함해야 합니다.");
        }
        
        if (!Pattern.matches(".*[A-Z].*", password)) {
            throw new MemberException("비밀번호는 대문자를 포함해야 합니다.");
        }
        
        if (!Pattern.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*", password)) {
            throw new MemberException("비밀번호는 특수문자를 포함해야 합니다.");
        }
    }
} 