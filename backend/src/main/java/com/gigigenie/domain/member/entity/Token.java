package com.gigigenie.domain.member.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Getter
@Entity
@NoArgsConstructor
@ToString(exclude = "member")
@Table(name = "token")
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 1000)
    private String accessToken;

    @Size(max = 1000)
    private String refreshToken;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "expire_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime expireAt;   // refreshToken 만료 시간


    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.expireAt = LocalDateTime.now().plusDays(1);    // refreshToken 24시간 이후 만료
    }

}
