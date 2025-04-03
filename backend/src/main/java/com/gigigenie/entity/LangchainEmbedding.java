package com.gigigenie.entity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "langchain_pg_embedding")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LangchainEmbedding {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 벡터 데이터 ID

    @ManyToOne
    @JoinColumn(name = "collection_id", nullable = false)
    private LangchainCollection collection; // 문서 ID (FK)

    @Column(nullable = false, columnDefinition = "vector(1536)")
    private String embedding; // 임베딩 데이터

    @Column(nullable = false, columnDefinition = "TEXT")
    private String document; // 추출된 텍스트

    @Column(nullable = false, columnDefinition = "jsonb")
    private String cmetadata; // 메타데이터
}
