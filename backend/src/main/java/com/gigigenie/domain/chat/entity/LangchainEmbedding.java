package com.gigigenie.domain.chat.entity;

import com.gigigenie.domain.chat.util.VectorType;
import com.vladmihalcea.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

import java.util.List;
import java.util.Map;

@Entity
@Table(name = "langchain_pg_embedding")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LangchainEmbedding {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "collection_id", nullable = false)
    private LangchainCollection collection;

    @Type(VectorType.class)
    @Column(name = "embedding", columnDefinition = "vector", nullable = false)
    private List<Float> embedding;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String document;

    @Type(JsonType.class)
    @Column(name = "cmetadata", columnDefinition = "jsonb")
    private Map<String, Object> cmetadata;

    @Column(name = "custom_id", columnDefinition = "TEXT")
    private String customId;
}
