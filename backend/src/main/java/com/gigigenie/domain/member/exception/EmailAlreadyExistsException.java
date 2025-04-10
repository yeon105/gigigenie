package com.gigigenie.domain.member.exception;

public class EmailAlreadyExistsException extends MemberException {
    public EmailAlreadyExistsException(String email) {
        super("이미 사용중인 이메일입니다: " + email);
    }
} 