package com.gigigenie.domain.member.service;

import com.gigigenie.domain.member.dto.JoinRequestDTO;
import com.gigigenie.domain.member.entity.Member;
import com.gigigenie.security.MemberDTO;

import java.util.Map;

public interface MemberService {

    void join(JoinRequestDTO request);

    boolean isEmailDuplicate(String email);

    Map<String, Object> login(String id, String password);

}

