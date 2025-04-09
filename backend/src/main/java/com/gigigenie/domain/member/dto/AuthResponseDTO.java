package com.gigigenie.domain.member.dto;

import com.gigigenie.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponseDTO {
    private String token;
    private Member member;
}
