package com.gigigenie.domain.chat.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;
import com.vladmihalcea.hibernate.type.json.JsonType;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "langchain_pg_embedding")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LangchainEmbedding {

    @Id
    @Column(nullable = false)
    private UUID uuid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "collection_id", nullable = false)
    private LangchainCollection collection;

    @JdbcTypeCode(SqlTypes.OTHER)
    @Column(columnDefinition = "vector", nullable = false)
    private List<Float> embedding;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String document;

    @Type(JsonType.class)
    @Column(name = "cmetadata", columnDefinition = "jsonb")
    private Map<String, Object> cmetadata;

    @Column(name = "custom_id", columnDefinition = "TEXT")
    private String customId;
}
