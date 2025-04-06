package com.gigigenie.domain.ai.entity;

import com.vladmihalcea.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;

import java.util.List;
import java.util.Map;

@Entity
@Table(name = "langchain_pg_embedding")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LangchainEmbedding {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "collection_id", nullable = false)
    private LangchainCollection collection;

    @JdbcTypeCode(SqlTypes.OTHER)
    @Column(columnDefinition = "vector")
    private List<Float> embedding;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String document;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> cmetadata;
}
