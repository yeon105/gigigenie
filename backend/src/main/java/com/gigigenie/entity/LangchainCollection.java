package com.gigigenie.entity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "langchain_pg_collection")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LangchainCollection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uuid; // 문서 ID

    @Column(nullable = false)
    private String name; // 파일명

    @Column(nullable = false, columnDefinition = "jsonb")
    private String cmetadata; // 메타데이터
}
