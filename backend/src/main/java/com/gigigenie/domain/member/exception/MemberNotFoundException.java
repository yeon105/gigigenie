package com.gigigenie.domain.member.exception;

public class MemberNotFoundException extends MemberException {
    public MemberNotFoundException(String email) {
        super("존재하지 않는 사용자입니다: " + email);
    }
} 