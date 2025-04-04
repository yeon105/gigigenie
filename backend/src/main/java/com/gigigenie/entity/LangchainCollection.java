package com.gigigenie.entity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "langchain_pg_collection")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LangchainCollection {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // UUID 자동 생성
    @Column(columnDefinition = "UUID", updatable = false, nullable = false)
    private UUID uuid; // UUID 타입으로 변경

    @Column(nullable = false)
    private String name; // 파일명

    @Column(nullable = false, columnDefinition = "jsonb")
    private String cmetadata; // 메타데이터
}
