package com.gigigenie.entity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Table(name = "product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId; // 제품 ID

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category; // 카테고리 ID (FK)

    @Column(nullable = false)
    private String modelName; // 모델명

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private Timestamp createdAt; // 등록 시간

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member; // 등록한 사용자
}

