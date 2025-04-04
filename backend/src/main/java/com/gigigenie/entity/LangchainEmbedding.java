package com.gigigenie.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gigigenie.converter.EmbeddingConverter;
import com.gigigenie.dto.DocumentDataDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "langchain_pg_embedding")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LangchainEmbedding {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "UUID", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "collection_id", nullable = false)
    private LangchainCollection collection;

    @Convert(converter = EmbeddingConverter.class) // ✅ JSON 변환기 적용
    @Column(nullable = false, columnDefinition = "TEXT") // 🔹 PostgreSQL에 JSON 문자열로 저장
    private float[] embedding;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String document;

    @Column(nullable = false, columnDefinition = "jsonb")
    private String cmetadata;

    @JsonIgnore
    public DocumentDataDTO toDTO() {
        DocumentDataDTO dto = new DocumentDataDTO();
        dto.setDocument(this.document);
        dto.setMetadata(this.cmetadata);
        return dto;
    }
}
