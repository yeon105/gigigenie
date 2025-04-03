package com.gigigenie.entity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Table(name = "query_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QueryHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long queryId; // 질의 ID

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member; // 회원 ID (FK)

    @Column(nullable = false)
    private String queryText; // 질문 내용

    @Column(columnDefinition = "TEXT")
    private String responseText; // AI 응답 내용

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private Timestamp queryTime; // 질의 시간
}

