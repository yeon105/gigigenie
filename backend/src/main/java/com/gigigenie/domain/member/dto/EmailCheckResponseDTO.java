package com.gigigenie.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmailCheckResponseDTO {
    private boolean isDuplicate;
}
