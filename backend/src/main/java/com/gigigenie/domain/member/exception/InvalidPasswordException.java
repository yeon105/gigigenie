package com.gigigenie.domain.member.exception;

public class InvalidPasswordException extends MemberException {
    public InvalidPasswordException() {
        super("잘못된 비밀번호입니다.");
    }
} 