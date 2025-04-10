package com.gigigenie.domain.member.entity;

import com.gigigenie.domain.member.enums.MemberRole;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "member")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id", nullable = false)
    private Integer memberId;

    @Column(nullable = false, length = 255)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false, length = 6)
    private String name;

    @Enumerated(EnumType.STRING)
    private MemberRole role;

    @Column(name = "join_date", nullable = false)
    private LocalDateTime joinDate;
}

